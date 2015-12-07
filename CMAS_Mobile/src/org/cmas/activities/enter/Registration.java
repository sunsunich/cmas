package org.cmas.activities.enter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.cmas.Globals;
import org.cmas.R;
import org.cmas.service.RegistrationService;
import org.cmas.util.StringUtil;

public class Registration extends BaseEnterView {

    private final RegistrationService registrationService = beanContainer.getRegistrationService();

    public Registration() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setupHeader(
                getString(R.string.login_register)
                , navigationService.getLoggedOffActivity()
        );

        Button regButton = (Button) findViewById(R.id.bnt_register);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForward();
            }
        });
    }

    public void onClickForward() {
        final FragmentActivity activity = this;
        final String username = ((TextView) activity.findViewById(R.id.username_input)).getText().toString();
        final String password = ((TextView) activity.findViewById(R.id.password_input)).getText().toString();
        final String repeatPassword
                = ((TextView) activity.findViewById(R.id.repeat_password_input)).getText().toString();

        if (!username.matches(Globals.USER_NAME_REGEX)) {
            reportError(getString(R.string.username_input_error));
            return;
        }
        if (StringUtil.isTrimmedEmpty(password)) {
            reportError(getString(R.string.password_empty_error));
            return;
        }

        EnterTask enterTask = new EnterTask(
                "registerUser", username, password, activity
        ) {
            @Override
            protected void handleError(String message) {
                reportError(message);
            }

            @Override
            protected String prepareEnterTask() {
                return registrationService.registerUser(activity, username, password, repeatPassword);
            }
        };
        enterTask.execute(getIntent().getExtras());
    }
}
