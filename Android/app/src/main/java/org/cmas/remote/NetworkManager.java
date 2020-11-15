package org.cmas.remote;

import org.cmas.util.http.ssl.AliasKeyManager;

public interface NetworkManager {

    boolean isNetworkAvailable();

    AliasKeyManager getAliasKeyManager() throws Exception;
}
