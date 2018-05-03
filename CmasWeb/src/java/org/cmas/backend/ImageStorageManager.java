package org.cmas.backend;

import org.cmas.entities.PersonalCard;
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

    //to be used in web client
    @NotNull
    String getUserpicRoot();

    //to be used in web client
    @NotNull
    String getCardImagesRoot();

    //to be used in web client
    @NotNull
    String getLogbookEntryImagesRoot();
}
