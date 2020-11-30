package org.cmas.util.mail;

import java.io.File;

public class Attachment {

    private final String fileName;

    private final File file;

    public Attachment(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
