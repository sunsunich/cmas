package org.cmas.util.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import org.cmas.util.SerializationUtil;

import java.io.Serializable;
import java.util.Map;

public class BundleUtil {

    private static final String BUNDLE_PROPNAME_PREFIX_FOR_S = "Egosanum.b_";
    private static final String BUNDLE_PROPNAME_PREFIX_FOR_P = "Egosanum.bp_";

    public static void saveFlatBundle(SharedPreferences.Editor editor, Bundle bundle) throws Exception {
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.d(BundleUtil.class.getName(), "Bundle key=" + String.valueOf(key));
                if (key != null) {
                    Object value = bundle.get(key);
                    if (value instanceof Bundle) {
                        saveFlatBundle(editor, (Bundle) value);
                    } else if(value instanceof Parcelable){
                        Parcelable bundleValue=(Parcelable) value;
                        editor.putString(
                                BUNDLE_PROPNAME_PREFIX_FOR_P + key,
                                ParcelUtils.toString(bundleValue)
                        );
                    }
                    else {
                        Serializable bundleValue = (Serializable) value;
                        editor.putString(
                                BUNDLE_PROPNAME_PREFIX_FOR_S + key,
                                SerializationUtil.toString(bundleValue)
                        );
                    }
                }
            }
        }
    }

    public static void loadBundle(SharedPreferences sharedPreferences, Bundle bundle) throws Exception {
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            //        Log.d(BundleUtil.class.getName(), "key=" + String.valueOf(key));
            //        Log.d(BundleUtil.class.getName(), "value=" + String.valueOf(entry.getValue()));
            if (key != null){
                if(key.startsWith(BUNDLE_PROPNAME_PREFIX_FOR_S)) {
                    String value = (String) entry.getValue();
                    String originalKey = key.substring(BUNDLE_PROPNAME_PREFIX_FOR_S.length());
                    bundle.putSerializable(originalKey, SerializationUtil.fromString(value));
                }else if(key.startsWith(BUNDLE_PROPNAME_PREFIX_FOR_P)) {
                    String value = (String) entry.getValue();
                    String originalKey = key.substring(BUNDLE_PROPNAME_PREFIX_FOR_P.length());
                    bundle.putParcelable(originalKey, ParcelUtils.fromString(value));
                }
            }
        }
    }
}
