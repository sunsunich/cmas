package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.DiveSpec;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.ScubaTank;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.DiveSpecDao;
import org.cmas.presentation.dao.logbook.LogbookBuddieRequestDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.logbook.ScubaTankDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.ShaEncoder;
import org.cmas.util.StringUtil;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created on Aug 05, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookServiceImpl implements LogbookService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String SALT = "Wfdf$%@T#@c)(";

    @Autowired
    private LogbookBuddieRequestDao logbookBuddieRequestDao;

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private DiveSpecDao diveSpecDao;

    @Autowired
    private ScubaTankDao scubaTankDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Transactional
    @Override
    public long createOrUpdateRecord(Diver diver, LogbookEntry formObject) throws ParseException, StaleObjectStateException, IOException {
        Long spotId = null;
        if (formObject.getDiveSpot() != null) {
            spotId = formObject.getDiveSpot().getId();
        }
        DiveSpot diveSpot = null;
        if (spotId != null) {
            diveSpot = diveSpotDao.getById(spotId);
            if (diveSpot == null) {
                throw new RuntimeException();
            }
        }

        Long instructorId = null;
        if (formObject.getInstructor() != null) {
            instructorId = formObject.getInstructor().getId();
        }
        Diver instructor = null;
        if (instructorId != null) {
            instructor = diverDao.getModel(instructorId);
            if (instructor == null) {
                throw new RuntimeException();
            }
        }

        Set<Diver> oldBuddies = null;
        Diver oldInstructor = null;
        Set<Long> oldTankIds = null;
        long logbookEntryId = formObject.getId();
        LogbookEntry logbookEntry;
        DiveSpec diveSpec;
        boolean isNew = logbookEntryId == 0L;
        if (isNew) {
            logbookEntry = new LogbookEntry();
            logbookEntry.setDiver(diver);
            logbookEntry.setDateCreation(new Date());
            diveSpec = new DiveSpec();
            logbookEntry.setDiveSpec(diveSpec);
        } else {
            logbookEntry = logbookEntryDao.getModel(logbookEntryId);
            if (logbookEntry == null) {
                throw new RuntimeException();
            }
            if (!StringUtil.isTrimmedEmpty(logbookEntry.getDigest())) {
                throw new RuntimeException();
            }
            diveSpec = logbookEntry.getDiveSpec();
            List<ScubaTank> oldTanks = diveSpec.getScubaTanks();
            if (oldTanks != null) {
                oldTankIds = new HashSet<>(oldTanks.size());
                for (ScubaTank scubaTank : oldTanks) {
                    oldTankIds.add(scubaTank.getId());
                }
            }

            oldInstructor = logbookEntry.getInstructor();
            if (oldInstructor != null && !oldInstructor.equals(instructor)) {
                LogbookBuddieRequest oldInstructorRequest
                        = logbookBuddieRequestDao.getLogbookBuddieRequest(logbookEntry, diver, oldInstructor);
                if (oldInstructorRequest != null) {
                    logbookBuddieRequestDao.deleteModel(oldInstructorRequest);
                }
            }
            oldBuddies = logbookEntry.getBuddies();
            if (oldBuddies != null && !oldBuddies.isEmpty()) {
                //manual fetching
                List<Long> buddiesIds = new ArrayList<>(oldBuddies.size());
                for (Diver buddie : oldBuddies) {
                    buddiesIds.add(buddie.getId());
                }
                oldBuddies = new HashSet<>(diverDao.getDiversByIds(buddiesIds));
            }
            logbookEntry.setBuddies(Collections.<Diver>emptySet());
            logbookEntryDao.updateModel(logbookEntry);
        }

        logbookEntry.setDateEdit(new Date());
        logbookEntry.setInstructor(instructor);
        logbookEntry.setDiveSpot(diveSpot);
        transfer(logbookEntry, formObject);
        DiveSpec foDiveSpec = formObject.getDiveSpec();
        transfer(diveSpec, foDiveSpec);

        Set<Diver> buddies = null;
        Set<Diver> foBuddies = formObject.getBuddies();
        if (foBuddies != null) {
            List<Long> buddiesIds = new ArrayList<>(foBuddies.size());
            for (Diver foBuddie : foBuddies) {
                buddiesIds.add(foBuddie.getId());
            }
            if (!buddiesIds.isEmpty()) {
                buddies = new HashSet<>(diverDao.getDiversByIds(buddiesIds));
                if (instructor != null) {
                    buddies.remove(instructor);
                }
                buddies.remove(diver);
                logbookEntry.setBuddies(buddies);
            }
        }
        if (oldBuddies == null) {
            oldBuddies = Collections.emptySet();
        }
        for (Diver buddie : oldBuddies) {
            if (buddies == null || !buddies.contains(buddie)) {
                LogbookBuddieRequest request =
                        logbookBuddieRequestDao.getLogbookBuddieRequest(logbookEntry, diver, buddie);
                if (request != null) {
                    logbookBuddieRequestDao.deleteModel(request);
                }
            }
        }

        if (isNew) {
            diveSpecDao.save(diveSpec);
            logbookEntryId = (Long) logbookEntryDao.save(logbookEntry);
        } else {
            logbookEntryDao.updateModel(logbookEntry);
            diveSpecDao.updateModel(diveSpec);
        }

        List<ScubaTank> foScubaTanks = foDiveSpec.getScubaTanks();
        if (foScubaTanks != null) {
            for (ScubaTank foScubaTank : foScubaTanks) {
                Long scubaTankId = foScubaTank.getId();
                if (scubaTankId == null) {
                    foScubaTank.setDiveSpec(diveSpec);
                    scubaTankDao.save(foScubaTank);
                } else {
                    ScubaTank dbScubaTank = scubaTankDao.getById(scubaTankId);
                    if (Objects.equals(dbScubaTank.getDiveSpec().getId(), diveSpec.getId())) {
                        transfer(dbScubaTank, foScubaTank);
                        scubaTankDao.updateModel(dbScubaTank);
                        if (oldTankIds != null) {
                            oldTankIds.remove(scubaTankId);
                        }
                    }
                }
            }
        }
        if (oldTankIds != null) {
            for (Long scubaTankId : oldTankIds) {
                scubaTankDao.deleteModel(scubaTankDao.getById(scubaTankId));
            }
        }

        if (instructor != null && !instructor.equals(oldInstructor)) {
            LogbookBuddieRequest request = new LogbookBuddieRequest();
            request.setLogbookEntry(logbookEntry);
            request.setFrom(diver);
            request.setTo(instructor);
            request.setToSign(true);
            Serializable requestId = logbookBuddieRequestDao.save(request);

            instructor.setSocialUpdatesVersion(instructor.getSocialUpdatesVersion() + 1L);
            diverDao.updateModel(instructor);
            mailService.sendToInstructorToApprove(logbookBuddieRequestDao.getById(requestId));
        }

        if (buddies != null) {
            for (Diver buddie : buddies) {
                if (!oldBuddies.contains(buddie)) {
                    @SuppressWarnings("ObjectAllocationInLoop")
                    LogbookBuddieRequest request = new LogbookBuddieRequest();
                    request.setLogbookEntry(logbookEntry);
                    request.setFrom(diver);
                    request.setTo(buddie);
                    logbookBuddieRequestDao.save(request);

                    buddie.setSocialUpdatesVersion(buddie.getSocialUpdatesVersion() + 1L);
                    //todo send notification to buddies
                    diverDao.updateModel(buddie);
                }
            }
        }

        return logbookEntryId;
    }

    private void transfer(LogbookEntry logbookEntry, LogbookEntry formObject) throws IOException {
        logbookEntry.setDiveDate(formObject.getDiveDate());
        logbookEntry.setPrevDiveDate(formObject.getPrevDiveDate());
        logbookEntry.setName(formObject.getName());
        logbookEntry.setLatitude(formObject.getLatitude());
        logbookEntry.setLongitude(formObject.getLongitude());
        logbookEntry.setScore(formObject.getScore());
        logbookEntry.setNote(formObject.getNote());
        logbookEntry.setVisibility(formObject.getVisibility());
        logbookEntry.setState(formObject.getState());
    }

    private static void transfer(DiveSpec diveSpec, DiveSpec formObject) {
        diveSpec.setDurationMinutes(formObject.getDurationMinutes());
        diveSpec.setMaxDepthMeters(formObject.getMaxDepthMeters());
        diveSpec.setAvgDepthMeters(formObject.getAvgDepthMeters());

        diveSpec.setWeather(formObject.getWeather());
        diveSpec.setSurface(formObject.getSurface());
        diveSpec.setCurrent(formObject.getCurrent());
        diveSpec.setUnderWaterVisibility(formObject.getUnderWaterVisibility());
        diveSpec.setWaterType(formObject.getWaterType());
        diveSpec.setWaterTemp(formObject.getWaterTemp());
        diveSpec.setAirTemp(formObject.getAirTemp());
        diveSpec.setTemperatureMeasureUnit(formObject.getTemperatureMeasureUnit());

        diveSpec.setDivePurpose(formObject.getDivePurpose());
        diveSpec.setEntryType(formObject.getEntryType());

        diveSpec.setAdditionalWeightKg(formObject.getAdditionalWeightKg());
        diveSpec.setDiveSuit(formObject.getDiveSuit());

        diveSpec.setIsApnea(formObject.getIsApnea());

        diveSpec.setDecoStepsComments(formObject.getDecoStepsComments());
        diveSpec.setHasSafetyStop(formObject.isHasSafetyStop());
        diveSpec.setCnsToxicity(formObject.getCnsToxicity());
    }

    private static void transfer(ScubaTank scubaTank, ScubaTank formObject) {
        scubaTank.setIsDecoTank(formObject.getIsDecoTank());
        scubaTank.setSize(formObject.getSize());
        scubaTank.setVolumeMeasureUnit(formObject.getVolumeMeasureUnit());
        scubaTank.setStartPressure(formObject.getStartPressure());
        scubaTank.setEndPressure(formObject.getEndPressure());
        scubaTank.setPressureMeasureUnit(formObject.getPressureMeasureUnit());
        scubaTank.setSupplyType(formObject.getSupplyType());
        scubaTank.setIsAir(formObject.getIsAir());
        scubaTank.setOxygenPercent(formObject.getOxygenPercent());
        scubaTank.setHeliumPercent(formObject.getHeliumPercent());
    }

    @Override
    public String createSignature(LogbookEntry entry) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        DiveSpec diveSpec = entry.getDiveSpec();
        //noinspection StringConcatenation
        return ShaEncoder.encode(entry.getName()
                                 + ShaEncoder.SEPARATOR
                                 + Globals.getDTFWebControls().format(entry.getDiveDate())
                                 + ShaEncoder.SEPARATOR
                                 + String.valueOf(entry.getLatitude())
                                 + ShaEncoder.SEPARATOR
                                 + String.valueOf(entry.getLongitude())
                                 + ShaEncoder.SEPARATOR
                                 + diveSpec.getIsApnea()
                                 + ShaEncoder.SEPARATOR
                                 + diveSpec.getMaxDepthMeters()
                                 + ShaEncoder.SEPARATOR
                                 + diveSpec.getDurationMinutes()
                                 + ShaEncoder.SEPARATOR
                                 + SALT);
    }
}
