package org.cmas.backend;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.UserFile;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created on Mar 12, 2018
 *
 * @author Alexander Petukhov
 */
public class LocalFileImageStorageManagerImpl implements ImageStorageManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int MAX_USER_PIC_WIDTH_PX = 344;

    private static final int MAX_USER_PIC_HEIGHT_PX = 360;

    private static final int MAX_LOGBOOK_ENTRY_IMAGE_WIDTH_PX = 1330;

    private static final int MAX_LOGBOOK_ENTRY_IMAGE_HEIGHT_PX = 770;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private UserFileDao userFileDao;

    private String imageStorageRoot;

    private String imageStorageRootLocation;

    @Override
    public void storeUserpic(Diver diver, BufferedImage initImage) throws IOException {
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_USER_PIC_WIDTH_PX, (float) MAX_USER_PIC_HEIGHT_PX
        );
        String userpicPath = Base64Coder.encodeString(String.valueOf(diver.getId()) + '_' + System.currentTimeMillis())
                             + ".png";
        File userpicFile = new File(getImageStoreLocationForUserpic() + userpicPath);

        ImageIO.write(scaledImage, "png", new FileOutputStream(userpicFile));
        String oldUserpicPath = diver.getUserpicUrl();
        diver.setUserpicUrl(userpicPath);
        diverDao.updateModel(diver);
        deleteOldFile(getImageStoreLocationForUserpic() + oldUserpicPath);
    }

    @Override
    public void storeCardImage(PersonalCard card, BufferedImage image) throws IOException {
        String cardPath;
        if (card.getCardType() == PersonalCardType.PRIMARY) {
            cardPath = card.getPrintNumber();
        } else {
            cardPath = String.valueOf(card.getId());
            if (!StringUtil.isTrimmedEmpty(card.getNumber())) {
                cardPath += '_' + card.getPrintNumber();
            }
        }
        cardPath += "_" + System.currentTimeMillis();
        cardPath = Base64Coder.encodeString(cardPath) + ".png";
        File cardFile = new File(getImageStoreLocationForCards() + cardPath);
        ImageIO.write(image, "png", new FileOutputStream(cardFile));
        String oldImagePath = card.getImageUrl();
        card.setImageUrl(cardPath);
        personalCardDao.updateModel(card);
        deleteOldFile(getImageStoreLocationForCards() + oldImagePath);
    }

    @Override
    public void storeLogbookEntryImage(LogbookEntry logbookEntry, BufferedImage initImage) throws IOException {
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_LOGBOOK_ENTRY_IMAGE_WIDTH_PX, (float) MAX_LOGBOOK_ENTRY_IMAGE_HEIGHT_PX
        );
        String photoPath = Base64Coder.encodeString(
                logbookEntry.getId() + "_" + logbookEntry.getDateEdit().getTime()
        ) + ".png";
        File photoFile = new File(getImageStoreLocationForLogbook() + photoPath);
        ImageIO.write(scaledImage, "png", new FileOutputStream(photoFile));
        String oldImagePath = logbookEntry.getPhotoUrl();
        logbookEntry.setPhotoUrl(photoPath);
        logbookEntryDao.updateModel(logbookEntry);
        deleteOldFile(getImageStoreLocationForLogbook() + oldImagePath);
    }

    @Override
    public void deleteImage(LogbookEntry logbookEntry) {
        String photoUrl = logbookEntry.getPhotoUrl();
        deleteOldFile(getImageStoreLocationForLogbook() + photoUrl);
        logbookEntry.setPhotoUrl(null);
        logbookEntryDao.updateModel(logbookEntry);
    }

    @Override
    public void storeUserFile(UserFile userFile, BufferedImage initImage) throws IOException {
        String imageStoreLocation = getImageStoreLocation(userFile);
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_LOGBOOK_ENTRY_IMAGE_WIDTH_PX, (float) MAX_LOGBOOK_ENTRY_IMAGE_HEIGHT_PX
        );
        userFile.setDateEdit(new Date());
        String photoPath = Base64Coder.encodeString(
                userFile.getId() + "_" + userFile.getDateEdit().getTime()
        ) + ".png";
        File photoFile = new File(imageStoreLocation + photoPath);
        ImageIO.write(scaledImage, "png", new FileOutputStream(photoFile));
        String oldImagePath = userFile.getFileUrl();
        userFile.setFileUrl(photoPath);
        userFileDao.updateModel(userFile);
        deleteOldFile(imageStoreLocation + oldImagePath);
    }

    @NotNull
    private String getImageStoreLocation(UserFile userFile) {
        String imageStoreLocation = imageStorageRootLocation
                                    + File.separatorChar
                                    + "unknown"
                                    + File.separatorChar;
        switch (userFile.getUserFileType()) {
            case LOGBOOK_ENTRY:
                imageStoreLocation = getImageStoreLocationForLogbook();
                break;
            case PERSONAL_CARD:
                imageStoreLocation = getImageStoreLocationForCards();
                break;
            case USERPIC:
                imageStoreLocation = getImageStoreLocationForUserpic();
                break;
            case FEEDBACK:
                imageStoreLocation = getImageStoreLocationForFeedback();
                break;
        }
        return imageStoreLocation;
    }

    @Override
    public void deleteUserFile(UserFile userFile) {
        deleteOldFile(getImageStoreLocation(userFile) + userFile.getFileUrl());
    }

    @NotNull
    private String getImageStoreLocationForUserpic() {
        return imageStorageRootLocation
               + File.separatorChar
               + "userpics"
               + File.separatorChar;
    }

    @NotNull
    private String getImageStoreLocationForLogbook() {
        return imageStorageRootLocation
               + File.separatorChar
               + "logbookEntries"
               + File.separatorChar;
    }

    @NotNull
    private String getImageStoreLocationForCards() {
        return imageStorageRootLocation
               + File.separatorChar
               + "cards"
               + File.separatorChar;
    }

    @NotNull
    private String getImageStoreLocationForFeedback() {
        return imageStorageRootLocation
               + File.separatorChar
               + "feedback"
               + File.separatorChar;
    }

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @NotNull
    @Override
    public String getCardImagesRoot() {
        return '/' + imageStorageRoot + "/cards/";
    }

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @NotNull
    @Override
    public String getFeedbackImagesRoot() {
        return '/' + imageStorageRoot + "/feedback/";
    }

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @NotNull
    @Override
    public String getLogbookEntryImagesRoot() {
        return '/' + imageStorageRoot + "/logbookEntries/";
    }

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @Override
    @NotNull
    public String getUserpicRoot() {
        return '/' + imageStorageRoot + "/userpics/";
    }

    @Required
    public void setImageStorageRoot(String imageStorageRoot) {
        this.imageStorageRoot = imageStorageRoot;
    }

    @Required
    public void setImageStorageRootLocation(String imageStorageRootLocation) {
        this.imageStorageRootLocation = imageStorageRootLocation;
    }

    private void deleteOldFile(String oldFile) {
        if (!StringUtil.isTrimmedEmpty(oldFile)) {
            try {
                if (!new File(oldFile).delete()) {
                    log.error("{} is not deleted", oldFile);
                }
            } catch (Exception e) {
                log.error("{} is not deleted, cause: {}", new Object[]{oldFile, e.getMessage()}, e);
            }
        }
    }
}
