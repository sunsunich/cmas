package org.cmas.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.entities.User;
import org.cmas.service.LoginService;
import org.cmas.service.UserService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;
import org.cmas.util.StringUtil;
import org.cmas.util.android.SecurePreferences;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 11:59
 */
public abstract class SecureFragment extends BaseFragment {

    protected final SettingsService settingsService = beanContainer.getSettingsService();
    protected final UserService userService = beanContainer.getUserService();
    protected final LoginService loginService = beanContainer.getLoginService();

    protected String currentUsername;
    protected User currentUser;

    protected SecureFragment(boolean storeState) {
        super(storeState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkUserLoggedIn();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUserLoggedIn();
    }

    protected void checkUserLoggedIn() {
        final FragmentActivity activity = getActivity();
        SharedPreferences sharedPreferences = new SecurePreferences(activity);
        Settings settings = settingsService.getSettings(sharedPreferences);
        currentUsername = settings.getCurrentUsername();

        if (StringUtil.isTrimmedEmpty(currentUsername)) {
            loaderLogout();
        } else {
            try {
                currentUser = userService.getByEmail(activity, currentUsername);
                if (currentUser == null) {
                    loaderLogout();
                }
            } catch (Exception e) {
                loaderLogout();
            }
        }
    }

    protected void loaderLogout() {
        final FragmentActivity activity = getActivity();
        DialogUtils.showLoaderDialog(getFragmentManager(), new ProgressTask<String>() {
            @Override
            public String doTask(OnPublishProgressListener listener) {
                loginService.logout(activity, currentUsername);
                return "";
            }

            @Override
            public void doAfterTask(String result) {

                Intent intent = new Intent(
                        activity,
                        navigationService.getLoggedOffActivity()
                );
                startActivity(intent);
                activity.finish();

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
