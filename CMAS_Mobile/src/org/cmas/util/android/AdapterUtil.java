package org.cmas.util.android;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;
import org.cmas.entities.Viewable;

public final class AdapterUtil {

    private AdapterUtil() {
    }

    public static void setTextDependingOnViewed(Viewable entity, TextView textView, String str){
        if(entity.isViewed()){
            textView.setText(str);
        } else {
            textView.setText(
                    makeBold(str)
            );
        }
    }

    public static SpannableString makeBold(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);

        return spannableString;
    }
}
