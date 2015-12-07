package org.cmas.tags;

import android.content.Context;
import android.util.AttributeSet;
import org.cmas.R;

public class BaseLayout extends AbstractBaseLayout {

    public BaseLayout(Context context) {
        super(context, R.layout.base);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.base);
    }
}
