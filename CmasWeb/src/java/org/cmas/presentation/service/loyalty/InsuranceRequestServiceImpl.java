package org.cmas.presentation.service.loyalty;

import org.cmas.Globals;
import org.cmas.entities.Address;
import org.cmas.entities.Country;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.dao.AddressDao;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.billing.PaidFeatureDao;
import org.cmas.presentation.dao.loyalty.InsuranceRequestDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.loyalty.bf.BalticFinanceResponse;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.dao.RunInHibernate;
import org.cmas.util.schedule.Scheduler;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.Nullable;
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
    private PaidFeatureDao paidFeatureDao;

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

    /*
    We could say the insurance starts tomorrow at 00:00 CET (the local time zone for Italy where CMAS is based).
    That way we don't need to worry where the diver currently is when signing up.
    I have a feeling if we do anything with UTC it will just confuse people.
    So saying you are covered from 18:55 UTC on the 4th of June 2019
    doesn't help many people to understand when the insurance starts.

    CMAS being from Italy, I think everybody will understand 5th of June 2019 00:00 Italian time zone.
     */
    // todo improve this rough estimate
    private static final long INSURANCE_LENGTH = 365 * Globals.ONE_DAY_IN_MS;

    @Nullable
    @Override
    public Date getDiverInsuranceExpiryDate(Diver diver) {
        InsuranceRequest insuranceRequest = insuranceRequestDao.getLatestPaidInsuranceRequestForDiver(diver.getId());
        if (insuranceRequest == null) {
            return null;
        } else {
            long expiryTime = insuranceRequest.getCreateDate().getTime() + INSURANCE_LENGTH;
            if (expiryTime > System.currentTimeMillis()) {
                return new Date(expiryTime);
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean canCreateInvoiceWithInsuranceRequest(Diver diver) {
        return insuranceRequestDao.getDraftByDiver(diver.getId()) != null && getDiverInsuranceExpiryDate(diver) == null;
    }

    // used by admin only
    @Override
    public void persistAndSendInsuranceRequest(InsuranceRequest rawRequest) {
        Long insuranceRequestId = saveNewInsuranceRequest(rawRequest);
        scheduleInsuranceRequest(insuranceRequestId);
    }

    @Override
    public void createInsuranceRequest(InsuranceRequest rawRequest) {
        InsuranceRequest draftRequest = insuranceRequestDao.getDraftByDiver(rawRequest.getDiver().getId());
        if (draftRequest == null) {
            saveNewInsuranceRequest(rawRequest);
        } else {
            draftRequest.setGender(rawRequest.getGender());
            updateInsuranceRequest(draftRequest, rawRequest);

            insuranceRequestDao.updateModel(draftRequest);
        }
    }

    @Override
    public void sendInsuranceRequest(Invoice invoice) {
        InsuranceRequest draftRequest = insuranceRequestDao.getDraftByDiver(invoice.getDiver().getId());
        if (draftRequest == null) {
            mailService.noInsuranceRequestForInvoice(invoice);
            LOG.error(
                    "InsuranceRequestService.sendInsuranceRequest: no draftInsuranceRequest for ExternalInvoiceNumber = "
                    + invoice.getExternalInvoiceNumber());
            return;
        }
        draftRequest.setInvoice(invoice);
        draftRequest.setCreateDate(new Date());
        insuranceRequestDao.updateModel(draftRequest);

        scheduleInsuranceRequest(draftRequest.getId());
    }

    private void updateInsuranceRequest(InsuranceRequest draftRequest, InsuranceRequest rawRequest) {
        draftRequest.setAddress(getDbAddress(rawRequest.getAddress()));
        draftRequest.setCreateDate(new Date());
        PaidFeature insurancePaidFeature = paidFeatureDao.getById(Globals.INSURANCE_PAID_FEATURE_DB_ID);
        draftRequest.setInsurancePrice(insurancePaidFeature.getPrice());
    }

    private Long saveNewInsuranceRequest(InsuranceRequest rawRequest) {
        Diver diver = diverDao.getModel(rawRequest.getDiver().getId());
        rawRequest.setDiver(diver);
        updateInsuranceRequest(rawRequest, rawRequest);

        return (Long) insuranceRequestDao.save(rawRequest);
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
            String message = "InsuranceRequestService: too many retry attempts for insuranceRequestId = "
                       + insuranceRequestId;
            mailService.sendInsuranceRequestFailed(dbRequest, message);
            LOG.error(message);
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
                String message = response.message == null ? response.rawJson : response.message;
                mailService.sendInsuranceRequestFailed(dbRequest, message);
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
                    sendInsuranceRequest(dbRequest.getId());
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
