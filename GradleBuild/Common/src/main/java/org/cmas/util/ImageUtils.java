package org.cmas.util;

import org.cmas.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);
    public static final String BASE_64_PREFIX = "base64,";

    private ImageUtils() {
    }

    public static ImageConversionResult base64ToImage(String imageBase64Bytes) {
        if (StringUtil.isTrimmedEmpty(imageBase64Bytes)) {
            return new ImageConversionResult("validation.emptyField");
        }
        byte[] imageBytes;
        try {
            if (imageBase64Bytes.contains(BASE_64_PREFIX)) {
                imageBase64Bytes = imageBase64Bytes.substring(
                        imageBase64Bytes.indexOf(BASE_64_PREFIX) + BASE_64_PREFIX.length()
                );
            }
            imageBytes = Base64Coder.decode(imageBase64Bytes);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ImageConversionResult("validation.imageFormat");
        }
        if ((long) imageBytes.length > Globals.MAX_IMAGE_SIZE) {
            return new ImageConversionResult("validation.imageSize");
        }
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                return new ImageConversionResult("validation.imageFormat");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return new ImageConversionResult("validation.imageFormat");
        }
        return new ImageConversionResult(image);
    }

    public static class ImageConversionResult {
        public final BufferedImage image;
        public final String errorCode;

        public ImageConversionResult(BufferedImage image) {
            this.image = image;
            errorCode = null;
        }

        public ImageConversionResult(String errorCode) {
            image = null;
            this.errorCode = errorCode;
        }
    }

    public static void main(String[] args) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("/Users/sunsunich/Downloads/base64.txt")));
     //   contents+='=';
      //  contents+='=';
        System.out.println(contents.length());
        System.out.println(contents.length() / 4.0);

        System.out.println(Base64Coder.decode(contents));

    }
}
