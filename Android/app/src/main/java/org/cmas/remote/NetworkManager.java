package org.cmas.remote;

import org.cmas.util.http.ssl.AliasKeyManager;

public interface NetworkManager {

    boolean isNetworkAvailable();

    // todo investigate and remove as well as bks keystore
    AliasKeyManager getAliasKeyManager() throws Exception;
}
