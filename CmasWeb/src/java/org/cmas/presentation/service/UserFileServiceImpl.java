package org.cmas.presentation.service;

import org.apache.commons.io.FilenameUtils;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.dao.user.RegFileDao;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Date;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public class UserFileServiceImpl implements UserFileService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserFileDao userFileDao;

    @Autowired
    private RegFileDao regFileDao;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Transactional
    @Override
    public UserFile copyFileFromRegistration(Diver diver, RegFile regFile) throws IOException {
        UserFile userFile = new UserFile();
        setupAndSaveUserFile(userFile, diver, UserFileType.CARD_APPROVAL_REQUEST, "");
        imageStorageManager.copyRegFileToUserFile(regFile, userFile);
        regFileDao.deleteModel(regFile);
        return userFile;
    }

    @Transactional
    @Override
    public UserFile processFile(Diver diver, UserFileType userFileType, MultipartFile multipartFile
//            , boolean fail
    ) throws UserFileException {
        UserFile userFile = new UserFile();
        try {
            String mimeType = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            setupAndSaveUserFile(userFile, diver, userFileType, mimeType);
//            if (fail) {
//                throw new RuntimeException();
//            }
            imageStorageManager.storeUserFile(userFile, ImageIO.read(multipartFile.getInputStream()));
            return userFile;
        } catch (Exception e) {
            processFileRollback(userFile);
            // throw exception to rollback transaction
            throw new UserFileException("validation.imageFormat", e.getMessage(), e);
        }
    }

    private void setupAndSaveUserFile(UserFile userFile, Diver diver, UserFileType userFileType, String mimeType) {
        userFile.setUserFileType(userFileType);
        userFile.setCreator(diver);
        userFile.setDateCreation(new Date());
        userFile.setDateEdit(new Date());
        userFile.setMimeType(mimeType);
        userFile.setFileUrl("");
        userFileDao.save(userFile);
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
