package org.cmas;

import android.content.SharedPreferences;

public class SettingsServiceImpl implements SettingsService {

    private static final String DEVICE_ID = "Egosanum.deviceId";
    private static final String GCM_REG_ID = "Egosanum.gcm.regid";
    private static final String CURRENT_USER_NAME = "Egosanum.currentUserName";
    private static final String CURRENT_PASSWORD = "Egosanum.currentPassword";
    private static final String JSESSIONID = "Egosanum.JSESSIONID";


    @Override
    public Settings getSettings(SharedPreferences preferences) {
        return new Settings(
              preferences.getString(DEVICE_ID, ""),
              preferences.getString(GCM_REG_ID, ""),
              preferences.getString(CURRENT_USER_NAME, ""),
              preferences.getString(CURRENT_PASSWORD, ""),
              preferences.getString(JSESSIONID, "")
        );
    }

    @Override
    public void setSettings(SharedPreferences preferences, Settings settings) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, settings.getDeviceId());
        editor.putString(GCM_REG_ID, settings.getGcmRegistrationId());
        editor.putString(CURRENT_USER_NAME, settings.getCurrentUsername());
        editor.putString(CURRENT_PASSWORD, settings.getCurrentPassword());
        editor.putString(JSESSIONID, settings.getJsessionid());
        editor.commit();
    }
}
