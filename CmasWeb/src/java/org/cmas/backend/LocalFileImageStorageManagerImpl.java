package org.cmas.backend;

import org.apache.commons.io.FileUtils;
import org.cmas.entities.UserFile;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.RegFileDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.cards.RegFile;
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

    @Autowired
    private RegFileDao regFileDao;

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
    public void storeCardImage(PersonalCard card, BufferedImage cardImage, BufferedImage qrImage) throws IOException {
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
        ImageIO.write(cardImage, "png", new FileOutputStream(cardFile));
        File qrFile = new File(getImageStoreLocationForCardsQR() + cardPath);
        ImageIO.write(qrImage, "png", new FileOutputStream(qrFile));

        String oldImagePath = card.getImageUrl();
        card.setImageUrl(cardPath);
        personalCardDao.updateModel(card);
        deleteOldFile(getImageStoreLocationForCards() + oldImagePath);
        deleteOldFile(getImageStoreLocationForCardsQR() + oldImagePath);
    }

    @Override
    public void storeRegCardImage(RegFile regFile, BufferedImage initImage) throws IOException {
        String imageStoreLocation = getImageStoreLocationForRegCards();
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_LOGBOOK_ENTRY_IMAGE_WIDTH_PX, (float) MAX_LOGBOOK_ENTRY_IMAGE_HEIGHT_PX
        );
        String path = Base64Coder.encodeString(String.valueOf(regFile.getId())) + ".png";
        File file = new File(imageStoreLocation + path);
        ImageIO.write(scaledImage, "png", new FileOutputStream(file));
        regFile.setFileUrl(path);
        regFileDao.updateModel(regFile);
    }

    @Override
    public void copyRegFileToUserFile(RegFile regFile, UserFile userFile) throws IOException {
        String photoPath = Base64Coder.encodeString(userFile.getId() + "_" + userFile.getDateEdit().getTime()) + ".png";
        String cardApprovalRequestPath = getImageStoreLocationForCardsApprovalRequests() + photoPath;

        String regFilePath = getImageStoreLocationForRegCards()
                             + Base64Coder.encodeString(String.valueOf(regFile.getId()))
                             + ".png";
        FileUtils.moveFile(new File(regFilePath), new File(cardApprovalRequestPath));
        userFile.setFileUrl(photoPath);
        userFileDao.updateModel(userFile);
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
        if (!StringUtil.isTrimmedEmpty(oldImagePath)) {
            deleteOldFile(imageStoreLocation + oldImagePath);
        }
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
            case PERSONAL_CARD_QR:
                imageStoreLocation = getImageStoreLocationForCardsQR();
                break;
            case USERPIC:
                imageStoreLocation = getImageStoreLocationForUserpic();
                break;
            case FEEDBACK:
                imageStoreLocation = getImageStoreLocationForFeedback();
                break;
            case CARD_APPROVAL_REQUEST:
                imageStoreLocation = getImageStoreLocationForCardsApprovalRequests();
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
    private String getImageStoreLocationForCardsQR() {
        return imageStorageRootLocation
               + File.separatorChar
               + "cards_qr"
               + File.separatorChar;
    }

    @NotNull
    private String getImageStoreLocationForRegCards() {
        return imageStorageRootLocation
               + File.separatorChar
               + "regCards"
               + File.separatorChar;
    }

    @NotNull
    private String getImageStoreLocationForCardsApprovalRequests() {
        return imageStorageRootLocation
               + File.separatorChar
               + "cardApprovalRequest"
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
    public String getCardApprovalRequestImagesRoot() {
        return '/' + imageStorageRoot + "/cardApprovalRequest/";
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
