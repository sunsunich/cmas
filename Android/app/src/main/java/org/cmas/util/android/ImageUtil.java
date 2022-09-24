package org.cmas.util.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import javax.annotation.Nullable;

public final class ImageUtil {

    private ImageUtil() {
    }

    @Nullable
    public static Bitmap createThumbnail(CreateImageThumbnailInput arg){
        String imagePath = arg.path;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int targetW = arg.width;
        int targetH = arg.height;

        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }
}
