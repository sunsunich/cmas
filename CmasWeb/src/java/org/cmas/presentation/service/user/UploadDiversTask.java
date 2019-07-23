package org.cmas.presentation.service.user;

import org.cmas.entities.Country;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.RunInHibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created on Aug 06, 2017
 *
 * @author Alexander Petukhov
 */
public class UploadDiversTask implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    volatile ScheduledFuture<?> future;
    private volatile boolean isRunning;
    private volatile int progress;
    private volatile Exception exception;

    private final DiverServiceImpl diverService;

    private final NationalFederation federation;
    private final InputStream file;


    UploadDiversTask(DiverServiceImpl diverService, NationalFederation federation, InputStream file) {
        this.diverService = diverService;
        this.federation = federation;
        this.file = file;
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            new RunInHibernate(diverService.sessionFactory) {
                @Override
                protected void runTaskInHibernate() {
                    try {
                        uploadDivers();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        exception = e;
                    }
                }
            }.run();
        } finally {
            isRunning = false;
        }
    }

    @SuppressWarnings("CallToStringEqualsIgnoreCase")
    private void uploadDivers() throws Exception {
        Country country = federation.getCountry();
        String countryCode = country.getCode();
        ProgressListener xlsProgressListener = new ProgressListener() {
            @Override
            public void updateProgress(int newPercentValue) {
                progress = newPercentValue / 5;
            }
        };
        if (Thread.currentThread().isInterrupted()) {
            return;
        }
        Collection<Diver> divers;
        if ("RUS".equalsIgnoreCase(countryCode)) {
            divers = diverService.rusDiverXlsParser.getDivers(file, xlsProgressListener);
        } else if ("IRI".equalsIgnoreCase(countryCode)) {
            divers = diverService.iranDiverXlsParser.getDivers(file, xlsProgressListener);
        } else {
            divers = diverService.singleTableDiverXlsParser.getDivers(file, xlsProgressListener);
        }
        if (divers == null) {
            throw new UnsupportedOperationException("Federation not supported");
        }
        int totalWork = divers.size();
        int workDone = 0;
        for (Diver diver : divers) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            diverService.uploadDiver(federation, diver, false);
            //hidden functionality creating or editing primary card number
            Diver dbDiver = diverService.diverDao.getByEmail(diver.getEmail());
            PersonalCard primaryPersonalCard = diver.getPrimaryPersonalCard();
            if (primaryPersonalCard != null) {
                String newNumber = primaryPersonalCard.getNumber();
                if (!StringUtil.isTrimmedEmpty(newNumber)) {
                    PersonalCard checkingCard = diverService.personalCardDao.getByNumber(newNumber);
                    if (checkingCard == null) {
                        // no such number found in DB
                        PersonalCard dbCard = dbDiver.getPrimaryPersonalCard();
                        boolean isNewCard = dbCard == null;
                        if (isNewCard || !dbCard.getNumber().equals(newNumber)) {
                            //number change or new number detected
                            try {
                                if (isNewCard) {
                                    dbCard = primaryPersonalCard;
                                    dbCard.setCardType(PersonalCardType.PRIMARY);
                                    dbCard.setDiverLevel(dbDiver.getDiverLevel());
                                    dbCard.setDiverType(dbDiver.getDiverType());
                                    primaryPersonalCard.setDiver(dbDiver);
                                    diverService.personalCardDao.save(dbCard);
                                    dbDiver.setPrimaryPersonalCard(primaryPersonalCard);
                                    diverService.diverDao.updateModel(dbDiver);
                                } else {
                                    dbCard.setImageUrl(null);
                                    dbCard.setNumber(newNumber);
                                    diverService.personalCardDao.updateModel(dbCard);
                                }
                                if (dbDiver.getDateReg() == null) {
                                    dbDiver.setDateReg(new Date());
                                    diverService.diverDao.updateModel(dbDiver);
                                }
                            } catch (Exception e) {
                                //number change or creation failed
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            workDone++;
            progress = 20 + workDone * 100 * 2 / totalWork / 5;
        }
        workDone = 0;
        for (Diver diver : divers) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            Diver dbDiver = diverService.diverDao.getByEmail(diver.getEmail());
            diverService.setDiverInstructor(federation, dbDiver, diver.getInstructor());
            workDone++;
            progress = StrictMath.min(60 + workDone * 100 * 2 / totalWork / 5, 99);
        }
        progress = 100;
    }

    public int getProgress() {
        return progress;
    }

    public Exception getException() {
        return exception;
    }

    // NOT_STARTED, IN_PROGRESS, ERROR, CANCELLED, DONE

    public UploadDiversTaskStatus getStatus() {
        if (future == null) {
            return UploadDiversTaskStatus.NOT_STARTED;
        }
        if (future.isCancelled()) {
            return UploadDiversTaskStatus.CANCELLED;
        }
        if (exception != null) {
            return UploadDiversTaskStatus.ERROR;
        }
        if (isRunning) {
            return UploadDiversTaskStatus.IN_PROGRESS;
        }
        // not running for some reason
        if (progress == 0) {
            return UploadDiversTaskStatus.NOT_STARTED;
        }
        if (progress < 100) {
            return UploadDiversTaskStatus.ERROR;
        } else {
            return UploadDiversTaskStatus.DONE;
        }
    }
}
