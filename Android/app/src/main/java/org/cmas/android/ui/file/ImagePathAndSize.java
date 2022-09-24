package org.cmas.android.ui.file;

import java.io.File;

public class ImagePathAndSize {

    public final String path;
    public final long fileSizeBytes;

    public ImagePathAndSize(String path) {
        this.path = path;
        this.fileSizeBytes = new File(path).length();
    }
}
