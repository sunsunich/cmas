package org.cmas.util.android;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * User: sunsunich
 * Date: 05/02/13
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class DeviceUuidFactory {

    private DeviceUuidFactory() {
    }

    public static UUID getDeviceUUID(Context context) {

        final String androidId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Use the Android ID unless it's broken, in which case fallback on deviceId,
        // unless it's not available, then fallback on a random number which we store
        // to a prefs file
        try {
            if ("9774d56d682e549c".equals(androidId)) {
                final
                String deviceId =
                        ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                return deviceId != null ?
                       UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) :
                       UUID.randomUUID();
            } else {
                return UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}
