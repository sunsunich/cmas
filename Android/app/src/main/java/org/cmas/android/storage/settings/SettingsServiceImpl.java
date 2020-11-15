package org.cmas.android.storage.settings;

import android.content.SharedPreferences;
import org.cmas.android.MainApplication;
import org.cmas.app.Settings;
import org.cmas.app.SettingsService;
import org.cmas.util.android.SecurePreferences;

public class SettingsServiceImpl implements SettingsService {

    private static final String DEVICE_ID = "org.cmas.android.deviceId";
    private static final String GCM_REG_ID = "org.cmas.android.gcm.regid";
    private static final String CURRENT_USER_NAME = "org.cmas.android.currentUserName";
    private static final String CURRENT_PASSWORD = "org.cmas.android.currentPassword";
    private static final String JSESSIONID = "org.cmas.android.JSESSIONID";


    @Override
    public Settings getSettings() {
        SharedPreferences preferences = getSharedPreferences();
        return new Settings(
                preferences.getString(DEVICE_ID, ""),
                preferences.getString(GCM_REG_ID, ""),
                preferences.getString(CURRENT_USER_NAME, ""),
                preferences.getString(CURRENT_PASSWORD, ""),
                preferences.getString(JSESSIONID, "")
        );
    }

    @Override
    public void setSettings(Settings settings) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, settings.getDeviceId());
        editor.putString(GCM_REG_ID, settings.getGcmRegistrationId());
        editor.putString(CURRENT_USER_NAME, settings.getCurrentUsername());
        editor.putString(CURRENT_PASSWORD, settings.getCurrentPassword());
        editor.putString(JSESSIONID, settings.getJsessionid());
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return new SecurePreferences(MainApplication.getAppContext());
    }
}
