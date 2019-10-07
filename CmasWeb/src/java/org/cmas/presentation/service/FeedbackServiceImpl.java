package org.cmas.presentation.service;

import org.apache.commons.io.FilenameUtils;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.FeedbackItem;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.FeedbackItemDao;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.model.FeedbackFormObject;
import org.cmas.presentation.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created on Oct 07, 2019
 *
 * @author Alexander Petukhov
 */
public class FeedbackServiceImpl implements FeedbackService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserFileDao userFileDao;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Autowired
    private FeedbackItemDao feedbackItemDao;

    @Autowired
    private MailService mailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processFeedback(Errors result, FeedbackFormObject feedbackFormObject, Diver diver, FeedbackItem feedbackItem) throws Exception {
        feedbackItem.setText(feedbackFormObject.getText());
        feedbackItem.setCreator(diver);

        List<UserFile> userFileList = new ArrayList<>();
        processFile(result, diver, userFileList, feedbackFormObject.getImage1(), "image1");//, false);
        processFile(result, diver, userFileList, feedbackFormObject.getImage2(), "image2");//, true);

        feedbackItem.setFiles(userFileList);
        feedbackItemDao.save(feedbackItem);

//        int i = 7;
//        if (i == 7) {
//            throw new RuntimeException();
//        }

        mailService.sendFeedbackItem(feedbackItem);
    }

    private void processFile(Errors result,
                             Diver diver,
                             Collection<UserFile> userFileList,
                             MultipartFile multipartFile,
                             String fieldName
//            , boolean fail
    ) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }
        UserFile userFile = null;
        try {
            userFile = new UserFile();
            userFile.setCreator(diver);
            userFile.setDateCreation(new Date());
            userFile.setDateEdit(new Date());
            userFile.setUserFileType(UserFileType.FEEDBACK);
            userFile.setMimeType(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
            userFile.setFileUrl("");
            userFileDao.save(userFile);

//            if (fail) {
//                throw new RuntimeException();
//            }

            imageStorageManager.storeUserFile(
                    userFile, ImageIO.read(multipartFile.getInputStream()));

            userFileList.add(userFile);
        } catch (Exception e) {
            result.rejectValue(fieldName, "validation.imageFormat");
            processFileRollback(userFile);
            for (UserFile userFileFromList : userFileList) {
                // must be !=, not equals(), because of hibernate proxies
                if (userFileFromList != userFile) {
                    processFileRollback(userFileFromList);
                }
            }
            // throw exception to rollback transaction
            throw e;
        }
    }

    @Override
    public void processFileRollback(UserFile userFile) {
        if (userFile == null || userFile.getFileUrl() == null || userFile.getUserFileType() == null) {
            return;
        }
        try {
            imageStorageManager.deleteUserFile(userFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
