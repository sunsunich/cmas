package org.cmas.backend;

import org.cmas.entities.diver.Diver;
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

    //to be used in web client
    @NotNull
    String getUserpicRoot();
}
