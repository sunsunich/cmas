package org.cmas.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.InitializingBean;
import org.cmas.R;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.entities.User;
import org.cmas.remote.ErrorCodes;
import org.cmas.remote.LoginData;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.util.ErrorCodeLocalizer;
import org.cmas.util.StringUtil;
import org.cmas.util.android.SecurePreferences;

public class LoginServiceImpl implements LoginService, InitializingBean {

    private UserService userService;
    private RemoteRegistrationService remoteRegistrationService;
    private SettingsService settingsService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        userService = beanContainer.getUserService();
        remoteRegistrationService = beanContainer.getRemoteRegistrationService();
        settingsService = beanContainer.getSettingsService();
    }

    @Override
    public Pair<User, String> loginUser(Context context, String email, String password) {
        User localUser;
        try {
            localUser = userService.getByEmail(context, email);
        } catch (Exception e) {
            Log.e(RegistrationServiceImpl.class.getName(), e.getMessage(), e);
            localUser = null;
        }
        boolean isNewUser = localUser == null;

        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings = settingsService.getSettings(sharedPreferences);

        try {
            Pair<User, String> loginResult = remoteRegistrationService.loginUsername(
                    context,
                    new LoginData(
                            email,
                            password,
                            settings.getDeviceId(),
                            settings.getGcmRegistrationId()
                    )
            );
            User remoteUser = loginResult.first;
            String errorMessageCode = loginResult.second;
            if (remoteUser == null) {
                if (isNewUser) {
                    return new Pair<User, String>(
                            null, ErrorCodeLocalizer.getLocalMessage(context, errorMessageCode, false)
                    );
                } else {
                    if (ErrorCodes.WRONG_PASSWORD.equals(errorMessageCode)) {
                        return new Pair<User, String>(
                                null, ErrorCodeLocalizer.getLocalMessage(context, errorMessageCode, false)
                        );
                    }
                    saveCredentials(context, email, password);
                    return new Pair<User, String>(localUser, "");
                }

            } else {
                long userId = remoteUser.getId();
                localUser = userService.getById(context, userId);
                isNewUser = localUser == null;

                remoteUser.setEmail(email);
                //TODO md5 password
                remoteUser.setPassword(password);
                userService.persistUser(context, remoteUser, isNewUser);

                User user = userService.getByEmail(context, email);
                saveCredentials(context, email, password);
                return new Pair<User, String>(user, "");
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
            //use local user if connection failed
            if (isNewUser) {
                return new Pair<User, String>(
                        null, context.getString(R.string.error_connecting_to_server)
                );
            } else {
                if (localUser.getPassword().equals(password)) {
                    saveCredentials(context, email, password);
                    return new Pair<User, String>(localUser, "");
                } else {
                    return new Pair<User, String>(
                            null, context.getString(R.string.wrong_password)
                    );
                }
            }
        }
    }

    private static final int LOGIN_ATTEMPTS = 5;

    @Override
    public void reLoginUserOnServer(Context context) throws ReLoginException {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings = settingsService.getSettings(sharedPreferences);
        String email = settings.getCurrentUsername();
        String password = settings.getCurrentPassword();
        if (StringUtil.isTrimmedEmpty(email) || StringUtil.isTrimmedEmpty(password)) {
            throw new ReLoginException("no credentials saved for user");
        }

        User localUser;
        try {
            localUser = userService.getByEmail(context, email);
        } catch (Exception e) {
            Log.e(RegistrationServiceImpl.class.getName(), e.getMessage(), e);
            localUser = null;
        }
        boolean isNewUser = localUser == null;
        try {
            LoginData loginData = new LoginData(
                    email,
                    password,
                    settings.getDeviceId(),
                    settings.getGcmRegistrationId()
            );
            int attemptCnt = 0;
            Pair<User, String> loginResult;
            do {
                loginResult = remoteRegistrationService.loginUsername(
                        context,
                        loginData
                );
                attemptCnt++;
            }
            while (loginResult.first == null && attemptCnt < LOGIN_ATTEMPTS);
            if (loginResult.first == null) {
                throw new ReLoginException("cannot relogin user, email=" + email + ", " +
                        "max amount of attempts reached ");
            } else {
                User remoteUser = loginResult.first;
                if (isNewUser) {
                    remoteUser.setEmail(email);
                    //TODO md5 password
                    remoteUser.setPassword(password);
                    userService.persistUser(context, remoteUser, true);
                }
            }
        } catch (Exception e) {
            throw new ReLoginException("cannot relogin user, email=" + email, e);
        }
    }

    private void saveCredentials(Context context, String username, String password) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings = settingsService.getSettings(sharedPreferences);
        settings.setCurrentUsername(username);
        settings.setCurrentPassword(password);
        settingsService.setSettings(sharedPreferences, settings);
    }

    @Override
    public void logout(Context context, String username) {

        //TODO remote call???
        //saveCredentials(context, "", "", 0L);
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings=settingsService.getSettings(sharedPreferences);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        settings.clear();
        settingsService.setSettings(sharedPreferences,settings);
    }
}
