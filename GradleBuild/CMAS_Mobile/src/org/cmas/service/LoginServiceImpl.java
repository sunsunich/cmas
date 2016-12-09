package org.cmas.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.InitializingBean;
import org.cmas.mobile.R;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.entities.diver.Diver;
import org.cmas.remote.ErrorCodes;
import org.cmas.remote.LoginData;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.util.ErrorCodeLocalizer;
import org.cmas.util.StringUtil;
import org.cmas.util.android.SecurePreferences;

public class LoginServiceImpl implements LoginService, InitializingBean {

    private DiverService diverService;
    private RemoteRegistrationService remoteRegistrationService;
    private SettingsService settingsService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        diverService = beanContainer.getDiverService();
        remoteRegistrationService = beanContainer.getRemoteRegistrationService();
        settingsService = beanContainer.getSettingsService();
    }

    @Override
    public Pair<Diver, String> loginUser(Context context, String email, String password) {
        Diver localDiver;
        try {
            localDiver = diverService.getByEmail(context, email);
        } catch (Exception e) {
            Log.e(RegistrationServiceImpl.class.getName(), e.getMessage(), e);
            localDiver = null;
        }
        boolean isNewDiver = localDiver == null;

        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings = settingsService.getSettings(sharedPreferences);

        try {
            Pair<Diver, String> loginResult = remoteRegistrationService.login(
                    context,
                    new LoginData(
                            email,
                            password,
                            settings.getDeviceId(),
                            settings.getGcmRegistrationId()
                    )
            );
            Diver remoteDiver = loginResult.first;
            String errorMessageCode = loginResult.second;
            if (remoteDiver == null) {
                if (isNewDiver) {
                    return new Pair<>(
                            null, ErrorCodeLocalizer.getLocalMessage(context, errorMessageCode, false)
                    );
                } else {
                    if (ErrorCodes.WRONG_PASSWORD.equals(errorMessageCode)) {
                        return new Pair<>(
                                null, ErrorCodeLocalizer.getLocalMessage(context, errorMessageCode, false)
                        );
                    }
                    saveCredentials(context, email, password);
                    return new Pair<>(localDiver, "");
                }

            } else {
                long userId = remoteDiver.getId();
                localDiver = diverService.getById(context, userId);
                isNewDiver = localDiver == null;

                remoteDiver.setEmail(email);
                //TODO md5 password
                remoteDiver.setPassword(password);
                diverService.persist(context, remoteDiver, isNewDiver);

                Diver diver = diverService.getByEmail(context, email);
                saveCredentials(context, email, password);
                return new Pair<>(remoteDiver, "");
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
            //use local user if connection failed
            if (isNewDiver) {
                return new Pair<>(
                        null, context.getString(R.string.error_connecting_to_server)
                );
            } else {
                if (localDiver.getPassword().equals(password)) {
                    saveCredentials(context, email, password);
                    return new Pair<>(localDiver, "");
                } else {
                    return new Pair<>(
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

        Diver localDiver;
        try {
            localDiver = diverService.getByEmail(context, email);
        } catch (Exception e) {
            Log.e(RegistrationServiceImpl.class.getName(), e.getMessage(), e);
            localDiver = null;
        }
        boolean isNewDiver = localDiver == null;
        try {
            LoginData loginData = new LoginData(
                    email,
                    password,
                    settings.getDeviceId(),
                    settings.getGcmRegistrationId()
            );
            int attemptCnt = 0;
            Pair<Diver, String> loginResult;
            do {
                loginResult = remoteRegistrationService.login(
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
                Diver remoteDiver = loginResult.first;
                if (isNewDiver) {
                    remoteDiver.setEmail(email);
                    //TODO md5 password
                    remoteDiver.setPassword(password);
                    diverService.persist(context, remoteDiver, true);
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
