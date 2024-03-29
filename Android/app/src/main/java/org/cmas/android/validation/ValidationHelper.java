package org.cmas.android.validation;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.cmas.android.MainApplication;
import org.cmas.ecards.R;
import org.cmas.util.StringUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ValidationHelper {

    private final List<ValidationItem> validationItems = new ArrayList<>();

    private boolean startSingleItemValidation;

    public ValidationHelper(boolean startSingleItemValidation) {
        this.startSingleItemValidation = startSingleItemValidation;
    }

    public void restoreValidationState() {
        if (startSingleItemValidation) {
            for (ValidationItem validationItem : validationItems) {
                validate(validationItem);
            }
        }
    }

    public boolean isStartSingleItemValidation() {
        return startSingleItemValidation;
    }

    public boolean validateAll() {
        startSingleItemValidation = true;
        boolean isAllValid = true;
        for (ValidationItem validationItem : validationItems) {
            // separate variable because of lazy computation
            boolean itemValid = validate(validationItem);
            isAllValid = isAllValid && itemValid;
        }
        return isAllValid;
    }

    public void setupEmptyValidation(EditText view, TextView errorView) {
        TextValidationView validationView = new TextValidationView(view);
        setupEmptyValidation(
                () -> view.getText().toString(),
                errorView,
                validationView
        );
    }

    public void setupEmptyValidation(Spinner view, TextView errorView, StringValueFromView input) {
        SpinnerValidationView validationView = new SpinnerValidationView(view);
        setupEmptyValidation(input, errorView, validationView);
    }

    private <T extends View> void setupEmptyValidation(
            StringValueFromView input, TextView errorView, ValidationView<T> validationView
    ) {
        addValidationItem(new ValidationItem(validationView, errorView) {
            @Nullable
            @Override
            public String validateForErrorText() {
                String value = input.getValue();
                if (StringUtil.isTrimmedEmpty(value)) {
                    return MainApplication.getAppContext().getString(R.string.error_empty_field);
                }
                return null;
            }
        });
    }

    public void addValidationItem(ValidationItem validationItem) {
        validationItems.add(validationItem);
        validationItem.validationView.addValueChangeListener(() -> {
            if (!startSingleItemValidation) {
                return;
            }
            validate(validationItem);
        });
    }

    private boolean validate(ValidationItem validationItem) {
        String errorText = validationItem.validateForErrorText();
        if (errorText == null) {
            validationItem.validationView.setBackgroundResource(R.drawable.edit_text_selector);
            validationItem.errorTextView.setVisibility(View.INVISIBLE);
            return true;
        } else {
            validationItem.validationView.setBackgroundResource(R.drawable.edit_text_error);
            validationItem.errorTextView.setText(errorText);
            validationItem.errorTextView.setVisibility(View.VISIBLE);
            return false;
        }
    }
}
