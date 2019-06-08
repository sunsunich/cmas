package org.cmas.presentation.service.loyalty;

import org.cmas.entities.Address;
import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.dao.AddressDao;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.loyalty.InsuranceRequestDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.loyalty.bf.BalticFinanceResponse;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.dao.RunInHibernate;
import org.cmas.util.schedule.Scheduler;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class InsuranceRequestServiceImpl implements InsuranceRequestService, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceRequestServiceImpl.class);

    @Autowired
    private InsuranceRequestDao insuranceRequestDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    @Qualifier("balticFinanceRemoteService")
    private InsuranceCompanyRemoteService insuranceCompanyRemoteService;

    @Autowired
    private MailService mailService;

    private static final long MIN_REQUEST_DELAY = 2000L;
    private static final long MAX_REQUEST_DELAY = 32000L;
    private final Map<Long, Long> requestsDelayMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        scheduler.schedule(new RunInHibernate(sessionFactory) {
            @Override
            public void runTaskInHibernate() {
                for (InsuranceRequest request : insuranceRequestDao.getAllUnsent()) {
                    sendInsuranceRequest(request.getId());
                }
            }
        }, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void persistAndSendInsuranceRequest(InsuranceRequest rawRequest) {
        rawRequest.setAddress(getDbAddress(rawRequest.getAddress()));

        Diver diver = diverDao.getModel(rawRequest.getDiver().getId());
        rawRequest.setDiver(diver);

        rawRequest.setCreateDate(new Date());
        Long insuranceRequestId = (Long) insuranceRequestDao.save(rawRequest);

        scheduleInsuranceRequest(insuranceRequestId);
    }

    private void scheduleInsuranceRequest(final Long insuranceRequestId) {
        scheduler.schedule(new RunInHibernate(sessionFactory) {
            @Override
            public void runTaskInHibernate() {
                sendInsuranceRequest(insuranceRequestId);
            }
        }, 0L, TimeUnit.MILLISECONDS);
    }

    private void sendInsuranceRequest(long insuranceRequestId) {
        final InsuranceRequest dbRequest = insuranceRequestDao.getModel(insuranceRequestId);
        if (dbRequest == null) {
            return;
        }
        Long requestDelay = requestsDelayMap.get(insuranceRequestId);
        if (requestDelay != null && requestDelay > MAX_REQUEST_DELAY) {
            //todo write email
            LOG.error("InsuranceRequestService: too many retry attempts for insuranceRequestId = "
                      + insuranceRequestId);
            requestsDelayMap.remove(insuranceRequestId);
            return;
        }
        try {
            BalticFinanceResponse response = insuranceCompanyRemoteService.sendInsuranceRequest(dbRequest);
            if (response.success) {
                dbRequest.setSent(true);
                dbRequest.setResponse(response.bfId);
                insuranceRequestDao.updateModel(dbRequest);
            } else {
                //todo write email
                String message = response.message == null ? response.rawJson : response.message;
                LOG.error("InsuranceRequestService: validation error insuranceRequestId = " + insuranceRequestId
                          + ". error message: " + message);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            if (requestDelay == null) {
                requestDelay = MIN_REQUEST_DELAY;
            } else {
                requestDelay *= 2L;
            }
            requestsDelayMap.put(insuranceRequestId, requestDelay);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    scheduleInsuranceRequest(dbRequest.getId());
                }
            }, requestDelay, TimeUnit.MILLISECONDS);
        }
    }

    // todo better synchronization
    private synchronized Address getDbAddress(Address rawAddress) {
        Address address = addressDao.getByRawAddress(rawAddress);
        if (address == null) {
            Country country = countryDao.getByCode(rawAddress.getCountry().getCode());
            rawAddress.setCountry(country);
            return addressDao.getModel(addressDao.save(rawAddress));
        } else {
            return address;
        }
    }
}
