package org.cmas.presentation.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Apr 13, 2016
 *
 * @author Alexander Petukhov
 */
public class FileUploadBean {

    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }
}
