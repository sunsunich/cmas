package org.cmas.android.validation;

import android.view.View;
import androidx.annotation.DrawableRes;
import org.cmas.util.android.ui.ValueChangeListener;

public abstract class ValidationView<VIEW extends View> {

    protected final VIEW viewToValidate;

    public ValidationView(VIEW viewToValidate) {
        this.viewToValidate = viewToValidate;
    }

    public void setBackgroundResource(@DrawableRes int resId){
        viewToValidate.setBackgroundResource(resId);
    }

    public abstract void addValueChangeListener(ValueChangeListener viewValueChangeListener);

}
