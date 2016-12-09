package org.cmas.activities.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.cmas.GCMIntentService;
import org.cmas.activities.Dispatcher;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.User;
import org.cmas.mobile.R;
import org.cmas.service.CodeService;
import org.cmas.util.LoaderTask;
import org.cmas.util.StringUtil;
import org.cmas.util.Task;

public class CodeEnter extends SecureActivity {

    private final CodeService codeService = beanContainer.getCodeService();

    private User user;

    public CodeEnter() {
        super(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                loaderLogout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AppLoader.dynamicLoadApp(this);

        setContentView(R.layout.code_enter);

        setupHeader(
                getString(R.string.code_enter_header)
                , null
        );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button regButton = (Button) findViewById(R.id.bnt_confirm);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForward();
            }
        });

        TextView codeInput1 = (TextView) findViewById(R.id.code_input1);
        TextView codeInput2 = (TextView) findViewById(R.id.code_input2);
        TextView codeInput3 = (TextView) findViewById(R.id.code_input3);
        TextView codeInput4 = (TextView) findViewById(R.id.code_input4);

        setInputFocusChange(regButton, codeInput1, codeInput2, codeInput3, codeInput4);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        user = (User) intent.getExtras().getSerializable(GCMIntentService.EGOSANUM_PUSH_PROFILE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (StringUtil.isTrimmedEmpty(currentUser.getMobileLockCode())) {
            Intent intent = new Intent(this, Dispatcher.class);
            intent.putExtra(GCMIntentService.EGOSANUM_PUSH_PROFILE, user);
            startActivity(intent);
            finish();
        } else {
            View restoreView = findViewById(R.id.restore_ll);
            if (restoreView != null) {
                restoreView.setVisibility(View.GONE);
            }
        }
    }

    public void onClickForward() {
        final Activity activity = this;

        TextView codeInput1 = (TextView) activity.findViewById(R.id.code_input1);
        TextView codeInput2 = (TextView) activity.findViewById(R.id.code_input2);
        TextView codeInput3 = (TextView) activity.findViewById(R.id.code_input3);
        TextView codeInput4 = (TextView) activity.findViewById(R.id.code_input4);

        final String code = evalCode(codeInput1, codeInput2, codeInput3, codeInput4);
        new LoaderTask<>(
                activity,
                new Task<String>() {

                    @Override
                    public String doTask() {
                        return codeService
                                .checkCode(activity, currentUsername, code);
                    }

                    @Override
                    public void doAfterTask(String result) {
                        if (StringUtil.isTrimmedEmpty(result)) {
                            Intent intent = new Intent(activity, Dispatcher.class);
                            intent.putExtra(GCMIntentService.EGOSANUM_PUSH_PROFILE, user);
                            activity.startActivity(intent);
                            finish();
                        } else {
                            reportError(result);
                        }
                    }

                    @Override
                    public void handleError() {
                        reportError(activity.getString(R.string.fatal_error));
                    }

                    @Override
                    public String getName() {
                        return "checkCode";
                    }
                }
        ).execute(1);
    }



    protected void setInputFocusChange(View endElement,
                                       TextView codeInput1,
                                       TextView codeInput2,
                                       TextView codeInput3,
                                       TextView codeInput4) {

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
                if (hasFocus) {
                    codeInput.setText("");
                }
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
        String code1 = codeInput1.getText().toString();
        String code2 = codeInput2.getText().toString();
        String code3 = codeInput3.getText().toString();
        String code4 = codeInput4.getText().toString();
        return code1 + code2 + code3 + code4;
    }


}
