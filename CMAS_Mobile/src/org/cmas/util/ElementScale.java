package org.cmas.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 18/01/13
 * Time: 19:20
 */
public class ElementScale {

    private ElementScale(){}

    public static void setElementHeight(View view, int screenHeightPX, float imageHeightPercent) {
        int maxHeight = Math.round((float) screenHeightPX * imageHeightPercent / 100.0f);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = maxHeight;
        view.setLayoutParams(layoutParams);
    }

    public static void setElementWidth(View view, int screenWidthPX, float imageWidthPercent) {
        int maxWidth = Math.round((float) screenWidthPX * imageWidthPercent / 100.0f);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = maxWidth;
        view.setLayoutParams(layoutParams);
    }

//    public static void scaleElement(View view, int maxSizePX, boolean isHeight) {
//        int width = view.getWidth();
//        int height = view.getHeight();
//        int bounding = maxSizePX;
//        Log.i("Test", "original width = " + Integer.toString(width));
//        Log.i("Test", "original height = " + Integer.toString(height));
//        Log.i("Test", "bounding = " + Integer.toString(bounding));
//
//        // Determine how much to scale: the dimension requiring less scaling is
//        // closer to the its side. This way the image always stays inside your
//        // bounding box AND either x/y axis touches it.
//        float xScale = ((float) bounding) / (float) width;
//        float yScale = ((float) bounding) / (float) height;
//        float scale = (isHeight) ? yScale : xScale;
//
//        int newWidth = Math.round((float) width * scale);
//        int newHeight = Math.round((float) height * scale);
//
//        Log.i("Test", "scaled width = " + Integer.toString(newWidth));
//        Log.i("Test", "scaled height = " + Integer.toString(newHeight));
//
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = newHeight;
//        layoutParams.width = newWidth;
//        view.setLayoutParams(layoutParams);
//    }
}
