package org.cmas;

import android.support.v4.app.FragmentActivity;

/**
 * Created on Jan 03, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("ConstantDeclaredInInterface")
public interface PermissionChecker {

    /**
     * Request code for location permission request.
     *
     */
    int LOCATION_PERMISSION_REQUEST_CODE = 1;


    boolean requestMapsPermissions(FragmentActivity activity);

    boolean isMapsPermissionsGranted(String[] permissions,
                                     int[] grantResults);
}
