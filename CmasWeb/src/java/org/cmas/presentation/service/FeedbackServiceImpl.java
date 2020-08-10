package org.cmas.presentation.service;

import org.cmas.entities.FeedbackItem;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.FeedbackItemDao;
import org.cmas.presentation.model.FeedbackFormObject;
import org.cmas.presentation.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created on Oct 07, 2019
 *
 * @author Alexander Petukhov
 */
public class FeedbackServiceImpl implements FeedbackService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackItemDao feedbackItemDao;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private MailService mailService;

    @Override
    public void processFeedback(Errors result, FeedbackFormObject feedbackFormObject, Diver diver, FeedbackItem feedbackItem) {
        List<UserFile> userFileList = new ArrayList<>();
        try {
            feedbackItem.setFiles(userFileList);
            transactionalProcessFeedback(result, feedbackFormObject, diver, feedbackItem);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            for (UserFile userFileFromList : userFileList) {
                userFileService.processFileRollback(userFileFromList);
            }
            if (!result.hasErrors()) {
                result.reject("validation.internal");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionalProcessFeedback(Errors result,
                                             FeedbackFormObject feedbackFormObject,
                                             Diver diver,
                                             FeedbackItem feedbackItem) throws Exception {
        feedbackItem.setText(feedbackFormObject.getText());
        feedbackItem.setCreator(diver);

        List<UserFile> userFileList = feedbackItem.getFiles();
        processFile(result, diver, userFileList, feedbackFormObject.getImage1(), "image1");//, false);
        processFile(result, diver, userFileList, feedbackFormObject.getImage2(), "image2");//, true);

        feedbackItemDao.save(feedbackItem);

//        int i = 7;
//        if (i == 7) {
//            throw new RuntimeException();
//        }

        mailService.sendFeedbackItem(feedbackItem);
        mailService.sendFeedbackItemToUser(feedbackItem);
    }

    private void processFile(Errors result,
                             Diver diver,
                             Collection<UserFile> userFileList,
                             MultipartFile multipartFile,
                             String fieldName
//            , boolean fail
    ) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }
        try {
            userFileList.add(userFileService.processFile(diver, UserFileType.FEEDBACK, multipartFile));
        } catch (UserFileException e) {
            result.rejectValue(fieldName, e.getErrorCode());
            // throw exception to rollback transaction
            throw e;
        }
    }
}
