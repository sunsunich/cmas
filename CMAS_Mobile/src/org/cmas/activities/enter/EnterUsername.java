package org.cmas.activities.enter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.cmas.Globals;
import org.cmas.R;
import org.cmas.util.StringUtil;

public class EnterUsername extends BaseEnterView {


    public EnterUsername() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		getSupportActionBar().hide();
		setContentView(R.layout.enter_username);
        Button enterButton = (Button) findViewById(R.id.bnt_enter);
        enterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickEnter();
            }
        });

        Button regButton = (Button) findViewById(R.id.bnt_register);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), navigationService.getRegistrationActivity());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClickEnter() {

        String username = ((TextView) findViewById(R.id.username_input)).getText().toString();
        if (!username.matches(Globals.EMAIL_REGEX)) {
            reportError(getString(R.string.email_error));
            return;
        }
        String password = ((TextView) findViewById(R.id.password_input)).getText().toString();
        if (StringUtil.isTrimmedEmpty(password)) {
            reportError(getString(R.string.password_empty_error));
            return;
        }

        FragmentActivity activity = this;
        EnterTask enterTask = new EnterTask(
                "loginUser", username, password, activity
        ) {
            @Override
            protected void handleError(String message) {
                reportError(message);
            }

            @Override
            protected String prepareEnterTask() {
                return "";
            }
        };
        enterTask.execute(getIntent().getExtras());

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
