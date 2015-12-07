package org.cmas.tags;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.cmas.R;

public abstract class AbstractBaseLayout extends LinearLayout {

    protected AbstractBaseLayout(Context context, int layoutId) {
        super(context);
        inflateLayout(context, layoutId);
    }

    protected AbstractBaseLayout(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs);
        inflateLayout(context, layoutId);
    }

    private void inflateLayout(Context context, int layoutId) {
        //Set this results to the layout defined in the xml layout
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(layoutId, this);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.main_container) {
            super.addView(child, params);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            ImageUtils.scaleImageHeight(
//                    (ImageView) findViewById(R.id.logo)
//                    , displayMetrics.heightPixels, 23.0f
//            );
//            ImageUtils.scaleImageHeight(
//                    (ImageView) findViewById(R.id.advert)
//                    , displayMetrics.heightPixels, 9.0f
//            );
        } else {
            ViewGroup contentWrapper = (ViewGroup) findViewById(R.id.content_wrapper);
            contentWrapper.addView(child, params);
        }

    }
}
