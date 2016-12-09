package org.cmas.fragments.settings;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.util.ElementScale;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 12:39
 */
public abstract class BaseSettingView extends BaseResultViewFragment {
    protected BaseSettingView(boolean storeState) {
        super(storeState);
    }

    protected void setInputFocusChange(final View endElement,
                                       final TextView codeInput1,
                                       final TextView codeInput2,
                                       final TextView codeInput3,
                                       final TextView codeInput4) {

        setListenersForCodeInput(null, codeInput1, codeInput2);
        setListenersForCodeInput(codeInput1, codeInput2, codeInput3);
        setListenersForCodeInput(codeInput2, codeInput3, codeInput4);
        setListenersForCodeInput(codeInput3, codeInput4, endElement);

    }

    private void setListenersForCodeInput(final View prevView,
                                          final TextView codeInput,
                                          final View nextView) {
        codeInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (codeInput.getText().length() == 1) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            nextView.requestFocus();

                        }
                    }, 500);
                }
            }
        });

        codeInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    /*codeInput.setText("");*/
                    ((EditText)codeInput).setSelection(codeInput.getText().length());
            }
        });

        if (prevView != null) {
            codeInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (codeInput.getText().length() == 0) {
                            prevView.requestFocus();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    protected String evalCode(TextView codeInput1, TextView codeInput2, TextView codeInput3, TextView codeInput4) {
        final String code1 = codeInput1.getText().toString();
        final String code2 = codeInput2.getText().toString();
        final String code3 = codeInput3.getText().toString();
        final String code4 = codeInput4.getText().toString();
        return code1 + code2 + code3 + code4;
    }


    protected void setInputCodesPosition(int maxWidth,
                                         float inputWidthPercent,
                                         TextView codeInput1,
                                         TextView codeInput2,
                                         TextView codeInput3,
                                         TextView codeInput4
    ) {
        float halfInputWidthPercent = inputWidthPercent / 2.0f;
        float halfInputWidth = (float) maxWidth * halfInputWidthPercent / 100.0f;
        float marginWidthPercent = (100.0f - 4.0F * inputWidthPercent - 3.0F * halfInputWidthPercent) / 2.0F;
        float marginWidth = (float) maxWidth * marginWidthPercent / 100.0f;

        ElementScale.setElementWidth(codeInput1, maxWidth, inputWidthPercent);
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) codeInput1.getLayoutParams();
            params.setMargins((int) marginWidth, params.topMargin, (int) halfInputWidth, params.bottomMargin);
            codeInput1.setLayoutParams(params);
        }

        ElementScale.setElementWidth(codeInput2, maxWidth, inputWidthPercent);
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) codeInput2.getLayoutParams();
            params.setMargins(0, params.topMargin, (int) halfInputWidth, params.bottomMargin);
            codeInput2.setLayoutParams(params);
        }
        ElementScale.setElementWidth(codeInput3, maxWidth, inputWidthPercent);
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) codeInput3.getLayoutParams();
            params.setMargins(0, params.topMargin, (int) halfInputWidth, params.bottomMargin);
            codeInput3.setLayoutParams(params);
        }
        ElementScale.setElementWidth(codeInput4, maxWidth, inputWidthPercent);
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) codeInput4.getLayoutParams();
            params.setMargins(0, params.topMargin, (int) marginWidth, params.bottomMargin);
            codeInput4.setLayoutParams(params);
        }
    }


}
