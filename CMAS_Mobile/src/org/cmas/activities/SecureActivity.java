package org.cmas.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cz.atria.barcode.encode.BarcodeEncoder;
import org.cmas.R;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.service.LoginService;
import org.cmas.service.UserService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;
import org.cmas.util.StringUtil;
import org.cmas.util.android.SecurePreferences;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class SecureActivity extends BaseActivity {

    public static void showQRCode(FragmentActivity activity, final Diver diver) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int width = StrictMath.round((float) displayMetrics.widthPixels * 0.8f);
            int height = StrictMath.round((float) displayMetrics.heightPixels * 0.3f);

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.view_qr_code);
            TextView qrCodeTitle = (TextView) dialog.findViewById(R.id.elem_text);
            String cardNumber = diver.getPrimaryPersonalCard().getNumber();
            qrCodeTitle.setText(
                    activity.getString(R.string.qr_code_title)
                            + '\n' + diver.getEmail()
                            + " ( " + cardNumber + " )"
            );
            ImageView qrCodeImageView = (ImageView) dialog.findViewById(R.id.qr_code);
            qrCodeImageView.setImageBitmap(
                    BarcodeEncoder.createQRCode(cardNumber, width, height)
            );

            Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            BaseActivity.reportError(activity, activity.getString(R.string.qr_code_error));
            Log.e(activity.getClass().getName(), e.getMessage(), e);
        }
    }

    protected final SettingsService settingsService = beanContainer.getSettingsService();
    protected final UserService userService = beanContainer.getUserService();
    protected final LoginService loginService = beanContainer.getLoginService();

    protected String currentUsername;
    protected User currentUser;


    protected SecureActivity(boolean storeState) {
        super(storeState);
    }

    private Intent backIntent;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                //startActivity(backIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected <B extends Activity> void setupHeader(
            String headerTextStr, Class<B> backActivityClass
    ) {
        setupHeader(headerTextStr, backActivityClass, Collections.<String, Serializable>emptyMap());
    }

    protected <B extends Activity> void setupHeader(String headerTextStr, Class<B> backActivityClass,
                                                    Map<String, Serializable> extraData) {
        getSupportActionBar().setTitle(makeHtmlTitle(headerTextStr));

        if (backActivityClass == null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            backIntent = new Intent(getBaseContext(), backActivityClass);
            for (Map.Entry<String, Serializable> item : extraData.entrySet()) {
                backIntent.putExtra(item.getKey(), item.getValue());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserLoggedIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLoggedIn();
    }

    protected void checkUserLoggedIn() {
        SharedPreferences sharedPreferences = new SecurePreferences(this);
        Settings settings = settingsService.getSettings(sharedPreferences);
        currentUsername = settings.getCurrentUsername();

        if (StringUtil.isTrimmedEmpty(currentUsername)) {
            openLoggedOffActivity();
        } else {
            try {
                currentUser = userService.getByEmail(this, currentUsername);
                if(currentUser == null){
                    logout();
                    return;
                }
            } catch (Exception e) {
                logout();
            }
        }
    }

    protected void logout() {
        loginService.logout(this, currentUsername);
        openLoggedOffActivity();
    }

    protected void openLoggedOffActivity(){
        Class<? extends Activity> loggedOffActivity = navigationService.getLoggedOffActivity();
        Intent intent = new Intent(
                getBaseContext(),
                loggedOffActivity
        );
        startActivity(intent);
        finish();

    }
    protected void loaderLogout() {
        DialogUtils.showLoaderDialog(getSupportFragmentManager(), new ProgressTask<String>() {
            @Override
            public String doTask(OnPublishProgressListener listener) {
                loginService.logout(SecureActivity.this, currentUsername);
                return "";
            }

            @Override
            public void doAfterTask(String result) {
                Intent intent = new Intent(
                        getBaseContext(),
                        navigationService.getLoggedOffActivity()
                );
                startActivity(intent);
                finish();
            }

            @Override
            public void handleError(String error) {
                reportError(error);
            }

            @Override
            public String getName() {
                return "logout";
            }
        });
    }
}
