package org.cmas.android.validation;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import org.cmas.util.android.ui.ValueChangeListener;

public class CheckedBoxValidationView extends ValidationView<CheckBox> {

    public CheckedBoxValidationView(CheckBox viewToValidate) {
        super(viewToValidate);
    }

    @Override
    public void setBackgroundResource(int resId) {
        // do nothing for CheckedTextView
    }

    public void addValueChangeListener(ValueChangeListener viewValueChangeListener) {
        viewToValidate.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            viewValueChangeListener.onValueChanged();
        });
    }
}
