package org.cmas.util.android;

import android.content.Context;
import android.os.PowerManager;

public class WakeLocker {

    private PowerManager.WakeLock wakeLock;

    public void acquire(Context context) {
        if (wakeLock != null) {
            wakeLock.release();
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                                  PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                  PowerManager.ON_AFTER_RELEASE, "WakeLock");
        wakeLock.acquire();
    }

    public void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
