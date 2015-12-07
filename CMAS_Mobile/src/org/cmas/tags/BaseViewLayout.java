package org.cmas.tags;

import android.content.Context;
import android.util.AttributeSet;
import org.cmas.R;

public class BaseViewLayout extends AbstractBaseLayout {

    public BaseViewLayout(Context context) {
        super(context, R.layout.base_view);
    }

    public BaseViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.base_view);
    }
}
