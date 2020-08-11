package org.cmas.presentation.service;

import org.apache.commons.io.FilenameUtils;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.dao.user.RegFileDao;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.util.ImageUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        String mimeType = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new UserFileException("validation.imageFormat", e.getMessage(), e);
        }
        return createUserFile(diver, userFileType, mimeType, bufferedImage);
    }

    @Transactional
    @Override
    public UserFile processFile(Diver diver, UserFileType userFileType, String base64data
//            , boolean fail
    ) throws UserFileException {
        ImageUtils.ImageConversionResult imageConversionResult = ImageUtils.base64ToImage(base64data);
        if (imageConversionResult.image == null) {
            throw new UserFileException(imageConversionResult.errorCode);
        }
        return createUserFile(diver, userFileType, "image/png", imageConversionResult.image);
    }

    @NotNull
    private UserFile createUserFile(Diver diver,
                                    UserFileType userFileType,
                                    String mimeType,
                                    BufferedImage bufferedImage)
            throws UserFileException {
        UserFile userFile = new UserFile();
        try {
            setupAndSaveUserFile(userFile, diver, userFileType, mimeType);
//            if (fail) {
//                throw new RuntimeException();
//            }
            imageStorageManager.storeUserFile(userFile, bufferedImage);
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
