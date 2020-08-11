package org.cmas.presentation.validator;

import org.cmas.Globals;
import org.cmas.util.ImageUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Oct 06, 2019
 *
 * @author Alexander Petukhov
 */
public class UploadImageValidator {

    private UploadImageValidator() {
    }

    public static String validateOptionalImage(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        return validateImage(multipartFile);
    }

    public static String validateImage(MultipartFile file) {
        if (file == null) {
            return "validation.emptyField";
        }
        if (!file.getContentType().startsWith("image")) {
            return "validation.imageFormat";
        }
        if (file.getSize() > Globals.MAX_IMAGE_SIZE) {
            return "validation.imageSize";
        }
        return null;
    }

    public static String validateBase64Image(String base64) {
        ImageUtils.ImageConversionResult imageConversionResult = ImageUtils.base64ToImage(base64);
        return imageConversionResult.errorCode;
    }
}
