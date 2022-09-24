package org.cmas.util.android;

public class CreateImageThumbnailInput {

    public final boolean isPhoto;
    public final String path;
    public final int width;
    public final int height;

    public CreateImageThumbnailInput(boolean isPhoto, String path, int width, int height) {
        this.isPhoto = isPhoto;
        this.path = path;
        this.width = width;
        this.height = height;
    }
}
