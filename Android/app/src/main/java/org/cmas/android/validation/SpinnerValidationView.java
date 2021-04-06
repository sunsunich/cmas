package org.cmas.android.validation;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import org.cmas.util.android.ui.ValueChangeListener;

public class SpinnerValidationView extends ValidationView<Spinner> {

    public SpinnerValidationView(Spinner viewToValidate) {
        super(viewToValidate);
    }

    public void addValueChangeListener(ValueChangeListener viewValueChangeListener) {
        viewToValidate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                viewValueChangeListener.onValueChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                viewValueChangeListener.onValueChanged();
            }

        });
    }

}
