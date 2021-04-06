package org.cmas.android.validation;

import android.widget.TextView;

import javax.annotation.Nullable;

public abstract class ValidationItem {

    public final ValidationView<?> validationView;
    public final TextView errorTextView;

    public ValidationItem(ValidationView<?> validationView, TextView errorTextView) {
        this.validationView = validationView;
        this.errorTextView = errorTextView;
    }

    @Nullable
    public abstract String validateForErrorText();
}
