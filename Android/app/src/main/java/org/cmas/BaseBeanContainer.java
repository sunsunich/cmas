package org.cmas;

import org.cmas.android.remote.NetworkManagerImpl;
import org.cmas.android.storage.convert.CountryPersisterImpl;
import org.cmas.android.storage.settings.SettingsServiceImpl;
import org.cmas.app.AppProperties;
import org.cmas.app.SettingsService;
import org.cmas.entities.Country;
import org.cmas.remote.NetworkManager;
import org.cmas.remote.RemoteDictionaryService;
import org.cmas.remote.RemoteDictionaryServiceImpl;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.remote.RemoteRegistrationServiceImpl;
import org.cmas.service.VersionableEntityPersister;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.service.dictionary.DictionaryDataServiceImpl;

@SuppressWarnings({
        "ClassWithTooManyFields",
        "ClassWithTooManyMethods",
        "OverlyCoupledClass"
})
public class BaseBeanContainer {

    private static final Object MONITOR = new Object();
    private static BaseBeanContainer instance;

    private AppProperties appProperties;
    private final SettingsService settingsService;
    private final NetworkManager networkManager;

    private final RemoteRegistrationServiceImpl remoteRegistrationService;
    private final RemoteDictionaryServiceImpl remoteDictionaryService;

    private final VersionableEntityPersister<Country> countryPersister;

    private final DictionaryDataServiceImpl dictionaryDataService;

    public static BaseBeanContainer getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (MONITOR) {
            if (instance == null) {
                instance = new BaseBeanContainer();
                //     instance.initialize();
            }
        }
        return instance;
    }

    private BaseBeanContainer() {
        settingsService = new SettingsServiceImpl();
        networkManager = new NetworkManagerImpl();

        remoteRegistrationService = new RemoteRegistrationServiceImpl();
        remoteDictionaryService = new RemoteDictionaryServiceImpl();

        countryPersister = new CountryPersisterImpl();

        dictionaryDataService = new DictionaryDataServiceImpl();
    }

    public void initialize() {
        remoteRegistrationService.initialize();
        remoteDictionaryService.initialize();

        dictionaryDataService.initialize();
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public RemoteRegistrationService getRemoteRegistrationService() {
        return remoteRegistrationService;
    }

    public RemoteDictionaryService getRemoteDictionaryService() {
        return remoteDictionaryService;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public VersionableEntityPersister<Country> getCountryPersister() {
        return countryPersister;
    }

    public DictionaryDataService getDictionaryDataService() {
        return dictionaryDataService;
    }
}

