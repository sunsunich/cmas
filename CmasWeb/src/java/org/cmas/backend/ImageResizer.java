package org.cmas.backend;

import com.mortennobel.imagescaling.ImageUtils;
import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created on Mar 12, 2018
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final class ImageResizer {

    private ImageResizer() {
    }

    public static BufferedImage scaleDownSavingProportions(BufferedImage imageToScale, float maxWidth, float maxHeight) {
        float initHeight = (float) imageToScale.getHeight();
        float initWidth = (float) imageToScale.getWidth();
        if (initHeight <= maxHeight && initWidth <= maxWidth) {
            return imageToScale;
        }
        float scaledWidth = initWidth;
        float scaledHeight = initHeight;
        if (initHeight > maxHeight) {
            scaledHeight = maxHeight;
            scaledWidth = maxHeight / initHeight * initWidth;
        }
        if (scaledWidth > maxWidth) {
            scaledHeight = maxWidth / scaledWidth * scaledHeight;
            scaledWidth = maxWidth;
        }
        return scaleDown(imageToScale, scaledWidth, scaledHeight);
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

    public static void main(String[] args) throws IOException {
//        generateAllBackgrounds("404",
//                                 "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/404_land_2888.png",
//                                 "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/404_portrait_960.png",
//                                 "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/404_long_portrait_1080.png",
//                                 "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/404_tab_portrait_1024.png"
//        );
//        generateAllBackgrounds("firstScreenBackground",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/firstScreenBackground_land_2840.png",
//                               null,
//                               null,
//                               null
//        );

//        generateAllBackgrounds("insurance",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/insurance_land_2133.png",
//                               new int[]{1920, 1680, 1440, 1280, 1024, 962, 840, 720, 640, 512, 480, 320, 240},
//                               null,
//                               null,
//                               null
//        );
//        generateAllBackgrounds("certificates",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/certificates_land_1065.png",
//                               new int[]{1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120},
//                               null,
//                               null,
//                               null
//        );
//        generateAllBackgrounds("buddies",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/buddies_land_1065.png",
//                               new int[]{1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120},
//                               null,
//                               null,
//                               null
//        );
//        generateAllBackgrounds("spots",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/spots_land_1065.png",
//                               new int[]{1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120},
//                               null,
//                               null,
//                               null
//        );
//        generateAllBackgrounds("memories",
//                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/memories_land_1065.png",
//                               new int[]{1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120},
//                               null,
//                               null,
//                               null
//        );
        generateAllBackgrounds("firstScreenFooter",
                               "/Users/sunsunich/workplace/сmas/CmasWeb/web/i/firstScreenFooter_land_2842.png",
                               null,
                               null,
                               null
        );
    }

    private static void generateAllBackgrounds(String baseName,
                                               String landImageFilePath,
                                               String portImageFilePath,
                                               String portLongImageFilePath,
                                               String portTabImageFilePath) throws IOException {
        int[] landWidthParams = {2560, 1920, 1680, 1440, 1280, 1024, 962, 640, 480};
        generateAllBackgrounds(baseName,
                               landImageFilePath,
                               landWidthParams,
                               portImageFilePath,
                               portLongImageFilePath,
                               portTabImageFilePath);
    }

    private static void generateAllBackgrounds(String baseName,
                                               String landImageFilePath,
                                               int[] landWidthParams,
                                               String portImageFilePath,
                                               String portLongImageFilePath,
                                               String portTabImageFilePath) throws IOException {
        File landImageFile = new File(landImageFilePath);
        generateFilesForAllWidth(baseName, landImageFile, landWidthParams, "land");

        if (portImageFilePath != null) {
            int[] portWidthParams = {800, 516, 320};
            File portImageFile = new File(portImageFilePath);
            generateFilesForAllWidth(baseName, portImageFile, portWidthParams, "portrait");
        }
        if (portLongImageFilePath != null) {
            int[] portLongWidthParams = {1080, 720, 601, 540, 480, 414, 375, 320};
            File portLongImageFile = new File(portLongImageFilePath);
            generateFilesForAllWidth(baseName, portLongImageFile, portLongWidthParams, "long_portrait");
        }
        if (portTabImageFilePath != null) {
            int[] portTabWidthParams = {1024, 834, 768};
            File portTabImageFile = new File(portTabImageFilePath);
            generateFilesForAllWidth(baseName, portTabImageFile, portTabWidthParams, "tab_portrait");
        }
    }

    private static void generateFilesForAllWidth(String baseName, File imageFile, int[] widthParams, String prefix) throws IOException {
        BufferedImage image = ImageIO.read(new FileInputStream(imageFile));
        for (int width : widthParams) {

            File outputFile = new File(imageFile.getParentFile().getAbsolutePath() + File.separatorChar + baseName +
                                       "_"
                                       + prefix
                                       + "_"
                                       + width + ".png");
            BufferedImage scaledImage = scaleDownSavingProportions(image, (float) width, Float.MAX_VALUE);
            ImageIO.write(scaledImage, "png", outputFile);
        }
    }
}
