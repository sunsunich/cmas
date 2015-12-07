package org.cmas;

import android.content.SharedPreferences;

public interface SettingsService {

    Settings getSettings(SharedPreferences preferences);

    void setSettings(SharedPreferences preferences, Settings settings);
}
