package org.cmas.presentation.service;

import org.apache.commons.io.FilenameUtils;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.UserFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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
    private ImageStorageManager imageStorageManager;

    @Transactional
    @Override
    public UserFile processFile(Diver diver, UserFileType userFileType, MultipartFile multipartFile
//            , boolean fail
    ) throws UserFileException {
        UserFile userFile = new UserFile();
        try {
            userFile.setUserFileType(userFileType);
            userFile.setCreator(diver);
            userFile.setDateCreation(new Date());
            userFile.setDateEdit(new Date());
            userFile.setMimeType(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
            userFile.setFileUrl("");
            userFileDao.save(userFile);
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
