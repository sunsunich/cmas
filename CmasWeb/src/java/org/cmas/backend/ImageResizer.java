package org.cmas.backend;

import com.mortennobel.imagescaling.ImageUtils;
import com.mortennobel.imagescaling.ResampleOp;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Created on Mar 12, 2018
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final class ImageResizer {

    private ImageResizer() {
    }

    public static BufferedImage scaleDownSavingProportions(BufferedImage imageToScale, float newHeight) {
        float newWidth = newHeight / (float) imageToScale.getHeight() * (float) imageToScale.getWidth();
        return scaleDown(imageToScale, newWidth, newHeight);
    }

    public static BufferedImage scaleDown(BufferedImage imageToScale, float newWidth, float newHeight) {
        int imageType;
        if (ImageUtils.nrChannels(imageToScale) > 0) {
            imageType = imageToScale.getType();
        } else {
            imageType = imageToScale.getColorModel().hasAlpha() ?
                    BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage after = new BufferedImage((int) newWidth, (int) newHeight, imageType);
        BufferedImageOp resampleOp = new ResampleOp((int) newWidth, (int) newHeight);
        resampleOp.filter(imageToScale, after);
        return after;
    }
}
