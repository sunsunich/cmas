package org.cmas.backend;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.jetbrains.annotations.NotNull;
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

    private static final int MAX_USER_PIC_HEIGHT_PX = 120;

    @Autowired
    private DiverDao diverDao;

    private String imageStorageRoot;

    private String imageStorageRootLocation;

    @Override
    public void storeUserpic(Diver diver, BufferedImage initImage) throws IOException {
        BufferedImage scaledImage = ImageResizer.scaleDownSavingProportions(
                initImage, (float) MAX_USER_PIC_HEIGHT_PX
        );
        String userpicPath = diver.getId() + ".png";
        File userpicFile = new File(imageStorageRootLocation
                                    + File.separatorChar
                                    + "userpics"
                                    + File.separatorChar
                                    + userpicPath);

        ImageIO.write(scaledImage, "png", new FileOutputStream(userpicFile));
        diver.setUserpicUrl(userpicPath);
        diverDao.updateModel(diver);
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
}
