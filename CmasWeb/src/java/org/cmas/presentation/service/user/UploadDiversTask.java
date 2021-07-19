package org.cmas.presentation.service.user;

import org.cmas.entities.Country;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.RunInHibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private volatile int diversProcessed;
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
        boolean isEgypt = Country.EGYPT_COUNTRY_CODE.equalsIgnoreCase(countryCode);
        if (Country.RUSSIAN_COUNTRY_CODE.equalsIgnoreCase(countryCode)) {
            divers = diverService.rusDiverXlsParser.getDivers(file, xlsProgressListener);
        } else if (Country.IRAN_COUNTRY_CODE.equalsIgnoreCase(countryCode)) {
            divers = diverService.iranDiverXlsParser.getDivers(file, xlsProgressListener);
        }
        else if (isEgypt) {
            divers = diverService.egyptDiverXlsParser.getDivers(file, xlsProgressListener);
        }
        else {
            divers = diverService.singleTableDiverXlsParser.getDivers(file, xlsProgressListener);
        }
        if (divers == null) {
            throw new UnsupportedOperationException("Federation not supported");
        }
        int totalWork = divers.size();
        Map<Long, DiverModificationData> dbDiversToModificationData = new HashMap<>(divers.size());
        for (Diver diver : divers) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            @Nullable
            Diver dbDiver;
            if (isEgypt) {
                if(diverService.uploadEgyptianDiver(diver)) {
                    dbDiver = diverService.diverDao.getByEmail(diver.getEmail());
                } else {
                    dbDiver = null;
                }
            } else {
                diverService.uploadDiverFromXls(federation, diver);
                dbDiver = diverService.diverDao.getByEmailForAdmin(diver.getEmail());
                //hidden functionality creating or editing primary card number
                updatePrimaryCard(diver, dbDiver);
            }
            if (dbDiver != null) {
                long dbDiverId = dbDiver.getId();
                DiverModificationData diverModificationData = dbDiversToModificationData.get(dbDiverId);
                if (diverModificationData == null) {
                    dbDiversToModificationData.put(dbDiverId,
                                                   new DiverModificationData(dbDiver, diver.getInstructor())
                    );
                } else {
                    if (diver.getInstructor() != null) {
                        diverModificationData.instructor = diver.getInstructor();
                    }
                }
            }
            diversProcessed++;
            progress = 20 + diversProcessed * 100 * 2 / totalWork / 5;
        }
        int finalizationWorkDone = 0;
        for (Map.Entry<Long, DiverModificationData> entry : dbDiversToModificationData.entrySet()) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            DiverModificationData diverModificationData = entry.getValue();
            if (isEgypt) {
                diverService.finalizeExistingEgyptianDiver(federation, diverModificationData);
            } else {
                diverService.finalizeDiverFromXls(federation, diverModificationData);
            }
            finalizationWorkDone++;
            progress = StrictMath.min(60 + finalizationWorkDone * 100 * 2 / totalWork / 5, 99);
        }
        progress = 100;
    }

    private void updatePrimaryCard(Diver diver, Diver dbDiver) {
        PersonalCard primaryPersonalCard = diver.getPrimaryPersonalCard();
        if (primaryPersonalCard == null) {
            return;
        }
        String newNumber = primaryPersonalCard.getNumber();
        if (StringUtil.isTrimmedEmpty(newNumber)) {
            return;
        }
        PersonalCard checkingCard = diverService.personalCardDao.getByNumber(federation, newNumber);
        if (checkingCard != null) {
            return;
        }
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
                    dbCard.setNumber(newNumber);
                    diverService.personalCardDao.updateModel(dbCard);
                    diverService.personalCardService.generateAndSaveCardImage(dbCard.getId());
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

    public int getProgress() {
        return progress;
    }

    public int getDiversProcessed() {
        return diversProcessed;
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
