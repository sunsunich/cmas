package org.cmas;

import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

/**
 * Created on Jan 03, 2016
 *
 * @author Alexander Petukhov
 */
public class PermissionCheckerImpl implements PermissionChecker{
    @Override
    public boolean requestMapsPermissions(FragmentActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
            return false;
        }
    }

    @Override
    public boolean isMapsPermissionsGranted(String[] permissions,
                                            int[] grantResults) {
        return PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
