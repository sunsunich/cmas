package org.cmas.presentation.service.user;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.DiveScore;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookBuddieRequestDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.model.logbook.LogbookEntryFormObject;
import org.cmas.util.Base64Coder;
import org.cmas.util.ShaEncoder;
import org.cmas.util.StringUtil;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Aug 05, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookServiceImpl implements LogbookService {

    public static final String SALT = "Wfdf$%@T#@c)(";

    @Autowired
    private LogbookBuddieRequestDao logbookBuddieRequestDao;

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Transactional
    @Override
    public void createOrUpdateRecord(Diver diver, LogbookEntryFormObject formObject) throws ParseException, StaleObjectStateException {
        Long spotId = Long.parseLong(formObject.getSpotId());
        DiveSpot diveSpot = diveSpotDao.getById(spotId);
        if (diveSpot == null) {
            throw new RuntimeException();
        }
        String instructorIdStr = formObject.getInstructorId();
        Diver instructor = null;
        if (!StringUtil.isTrimmedEmpty(instructorIdStr)) {
            Long instructorId = Long.parseLong(instructorIdStr);
            instructor = diverDao.getModel(instructorId);
            if (instructor == null) {
                throw new RuntimeException();
            }
        }
        Set<Diver> oldBuddies = null;
        Diver oldInstructor = null;
        String logbookEntryIdStr = formObject.getLogbookEntryId();
        LogbookEntry logbookEntry;
        boolean isNew = StringUtil.isTrimmedEmpty(logbookEntryIdStr);
        if (isNew) {
            logbookEntry = new LogbookEntry();
            logbookEntry.setDiver(diver);
            logbookEntry.setDateCreation(new Date());
            logbookEntry.setDiveSpot(diveSpot);
        } else {
            Long logbookEntryId = Long.parseLong(logbookEntryIdStr);
            logbookEntry = logbookEntryDao.getModel(logbookEntryId);
            if (logbookEntry == null) {
                throw new RuntimeException();
            }
            if (!StringUtil.isTrimmedEmpty(logbookEntry.getDigest())) {
                throw new RuntimeException();
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
            if (oldBuddies != null) {
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
        transfer(logbookEntry, formObject);

        Set<Diver> buddies = null;
        List<Long> buddiesIds = new Gson().fromJson(formObject.getBuddiesIds(), Globals.LONG_LIST_TYPE);
        if (buddiesIds != null && !buddiesIds.isEmpty()) {
            buddies = new HashSet<>(diverDao.getDiversByIds(buddiesIds));
            if (instructor != null) {
                buddies.remove(instructor);
            }
            buddies.remove(diver);
            logbookEntry.setBuddies(buddies);
        }

        if (isNew) {
            logbookEntryDao.save(logbookEntry);
        } else {
            logbookEntryDao.updateModel(logbookEntry);
        }

        if (instructor != null && !instructor.equals(oldInstructor)) {
            LogbookBuddieRequest request = new LogbookBuddieRequest();
            request.setLogbookEntry(logbookEntry);
            request.setFrom(diver);
            request.setTo(instructor);
            request.setToSign(true);
            logbookBuddieRequestDao.save(request);

            instructor.setSocialUpdatesVersion(instructor.getSocialUpdatesVersion() + 1L);
            //todo send notification to instructor
        }

        if (buddies != null) {
            if (oldBuddies == null) {
                oldBuddies = Collections.emptySet();
            }
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
                }
            }
            for (Diver buddie : oldBuddies) {
                if (!buddies.contains(buddie)) {
                    LogbookBuddieRequest request =
                            logbookBuddieRequestDao.getLogbookBuddieRequest(logbookEntry, diver, buddie);
                    if (request != null) {
                        logbookBuddieRequestDao.deleteModel(request);
                    }
                }
            }
        }
    }

    private static LogbookEntry transfer(LogbookEntry logbookEntry, LogbookEntryFormObject formObject) throws ParseException {
        logbookEntry.setDiveDate(Globals.getDTF().parse(formObject.getDiveDate()));
        logbookEntry.setDurationMinutes(Integer.parseInt(formObject.getDuration()));
        logbookEntry.setDepthMeters(Integer.parseInt(formObject.getDepth()));

        logbookEntry.setNote(formObject.getNote());

        String score = formObject.getScore();
        if (!StringUtil.isTrimmedEmpty(score)) {
            logbookEntry.setScore(DiveScore.valueOf(score));
        }
        String visibility = formObject.getVisibility();
        if (!StringUtil.isTrimmedEmpty(visibility)) {
            logbookEntry.setVisibility(LogbookVisibility.valueOf(visibility));
        }
        String photo = formObject.getPhoto();
        if (!StringUtil.isTrimmedEmpty(photo)) {
            logbookEntry.setPhoto(Base64Coder.decode(photo));
        }
        return logbookEntry;
    }

    @Override
    public String createSignature(LogbookEntry entry) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //noinspection StringConcatenation
        return ShaEncoder.encode(entry.getDepthMeters()
                                 + ShaEncoder.SEPARATOR
                                 + Globals.getDTF().format(entry.getDiveDate())
                                 + ShaEncoder.SEPARATOR
                                 + entry.getDurationMinutes()
                                 + ShaEncoder.SEPARATOR
                                 + SALT);
    }

    @Override
    public String createSignature(LogbookEntryFormObject formObject) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {
        return createSignature(transfer(new LogbookEntry(), formObject));
    }
}
