package org.cmas.tags;

import android.content.Context;
import android.util.AttributeSet;
import org.cmas.R;

public class BaseSettingsLayout extends AbstractBaseLayout {

    public BaseSettingsLayout(Context context) {
        super(context, R.layout.base_settings);
    }

    public BaseSettingsLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.base_settings);
    }
}
