package org.cmas.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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

public abstract class SecureActivity extends BaseActivity {

    public static void showBarCode(FragmentActivity activity, final Diver diver) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int width = StrictMath.round((float) displayMetrics.widthPixels * 0.8f);
            int height = StrictMath.round((float) displayMetrics.heightPixels * 0.3f);

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.view_barcode);
            TextView barcodeTitle = (TextView) dialog.findViewById(R.id.elem_text);
            String profileNumber = diver.getPrimaryPersonalCard().getNumber();
            barcodeTitle.setText(
                    activity.getString(R.string.barcode_title)
                            + '\n' + diver.getEmail()
                            + " ( " + profileNumber + " )"
            );
            ImageView barcodeImageView = (ImageView) dialog.findViewById(R.id.barcode);
            barcodeImageView.setImageBitmap(
                    BarcodeEncoder.createBarcode(profileNumber, width, height)
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
            BaseActivity.reportError(activity, activity.getString(R.string.barcode_error));
            Log.e(activity.getClass().getName(), e.getMessage(), e);
        }
    }

    protected final SettingsService settingsService = beanContainer.getSettingsService();
    protected final UserService userService = beanContainer.getUserService();
    protected final LoginService loginService = beanContainer.getLoginService();

    protected String currentUsername;
    protected User currentUser;
    protected long profileId;


    protected SecureActivity(boolean storeState) {
        super(storeState);
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
