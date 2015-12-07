package org.cmas.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 15/01/13
 * Time: 23:30
 */
public class ImageUtils {

    private ImageUtils() {
    }

    public static void scaleImageHeight(ImageView view, int screenHeightPX, float imageHeightPercent) {
        scaleImage(view, Math.round((float) screenHeightPX * imageHeightPercent / 100.0f), true);
    }

    public static void scaleImageWidth(ImageView view, int screenWidthPX, float imageWidthPercent) {
        scaleImage(view, Math.round((float) screenWidthPX * imageWidthPercent / 100.0f), false);
    }

    public static int dpSize(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public static void scaleImage(ImageView view, int maxSizePX, boolean isHeight) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = maxSizePX;
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / (float) width;
        float yScale = ((float) bounding) / (float) height;
        float scale = (isHeight) ? yScale : xScale;
//        Log.i("Test", "xScale = " + Float.toString(xScale));
//        Log.i("Test", "yScale = " + Float.toString(yScale));
//        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    public static boolean imagesAreEqual(Bitmap bitmap1, Bitmap bitmap2)
    {
        if (bitmap1.getHeight() != bitmap2.getHeight()) {
            return false;
        }
        if (bitmap1.getWidth() != bitmap2.getWidth()) {
            return false;
        }


        for (int y = 0; y < bitmap1.getHeight(); y++) {
            for (int x = 0; x < bitmap1.getWidth(); x++) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
