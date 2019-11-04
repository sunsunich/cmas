package org.cmas.backend;

import org.cmas.entities.UserFile;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created on Mar 12, 2018
 *
 * @author Alexander Petukhov
 */
public interface ImageStorageManager {

    void storeUserpic(Diver diver, BufferedImage initImage) throws IOException;

    void storeCardImage(PersonalCard card, BufferedImage image) throws IOException;

    void storeLogbookEntryImage(LogbookEntry logbookEntry, BufferedImage initImage) throws IOException;

    void storeUserFile(UserFile userFile, BufferedImage initImage) throws IOException;

    void deleteImage(LogbookEntry logbookEntry);

    //to be used in web client
    @NotNull
    String getUserpicRoot();

    void deleteUserFile(UserFile userFile);

    //to be used in web client
    @NotNull
    String getCardImagesRoot();

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @NotNull
    String getCardApprovalRequestImagesRoot();

    @SuppressWarnings({"MagicCharacter", "HardcodedFileSeparator"})
    @NotNull
    String getFeedbackImagesRoot();

    //to be used in web client
    @NotNull
    String getLogbookEntryImagesRoot();
}
