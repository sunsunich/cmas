package org.cmas.backend;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
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

/**
 * Created on Mar 12, 2018
 *
 * @author Alexander Petukhov
 */
public class LocalFileImageStorageManagerImpl implements ImageStorageManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int MAX_USER_PIC_WIDTH_PX = 90;

    private static final int MAX_USER_PIC_HEIGHT_PX = 120;

    private static final int MAX_LOGBOOK_ENTRY_IMAGE_WIDTH_PX = 1330;

    private static final int MAX_LOGBOOK_ENTRY_IMAGE_HEIGHT_PX = 770;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    private String imageStorageRoot;

    private String imageStorageRootLocation;

    @Override
    public void storeUserpic(Diver diver, BufferedImage initImage) throws IOException {
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_USER_PIC_WIDTH_PX, (float) MAX_USER_PIC_HEIGHT_PX
        );
        String userpicPath = Base64Coder.encodeString(String.valueOf(diver.getId()) + "_" + System.currentTimeMillis())
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
                cardPath += "_" + card.getPrintNumber();
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
    @Override
    public String getCardImagesRoot() {
        return "/" + imageStorageRoot + "/cards/";
    }

    @NotNull
    @Override
    public String getLogbookEntryImagesRoot() {
        return "/" + imageStorageRoot + "/logbookEntries/";
    }

    @Override
    @NotNull
    public String getUserpicRoot() {
        return "/" + imageStorageRoot + "/userpics/";
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
