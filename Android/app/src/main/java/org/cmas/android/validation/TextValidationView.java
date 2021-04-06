package org.cmas.android.validation;

import android.widget.EditText;
import org.cmas.util.android.ui.TextChangedListener;
import org.cmas.util.android.ui.ValueChangeListener;

public class TextValidationView extends ValidationView<EditText>{

    public TextValidationView(EditText viewToValidate) {
        super(viewToValidate);
    }

    public void addValueChangeListener(ValueChangeListener viewValueChangeListener){
        viewToValidate.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onValueChanged() {
                viewValueChangeListener.onValueChanged();
            }
        });
    }

}
