package org.cmas.android.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.cmas.ecards.R;
import org.cmas.android.MainApplication;
import org.cmas.remote.NetworkManager;
import org.cmas.util.http.ssl.AliasKeyManager;

import java.io.InputStream;

public class NetworkManagerImpl implements NetworkManager {

    private static final String KEYSTORE_PASS = "oV^h6g1i^sL1";
    private static final String KEYSTORE_ALIAS = "cmas";

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                MainApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public AliasKeyManager getAliasKeyManager() throws Exception {
        try (InputStream stream = MainApplication.getAppContext().getResources().openRawResource(R.raw.android)) {
            return new AliasKeyManager(
                    stream
                    , KEYSTORE_PASS.toCharArray()
                    , KEYSTORE_ALIAS
            );
        }
    }
}
