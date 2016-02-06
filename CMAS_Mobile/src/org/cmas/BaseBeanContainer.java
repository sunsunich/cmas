package org.cmas;

import org.cmas.dao.CreateTableDao;
import org.cmas.dao.DiverDao;
import org.cmas.dao.DiverDaoImpl;
import org.cmas.dao.dictionary.CountryDao;
import org.cmas.dao.dictionary.CountryDaoImpl;
import org.cmas.dao.divespot.DiveSpotDao;
import org.cmas.dao.divespot.DiveSpotDaoImpl;
import org.cmas.dao.logbook.LogbookEntryDao;
import org.cmas.dao.logbook.LogbookEntryDaoImpl;
import org.cmas.remote.RemoteDictionaryService;
import org.cmas.remote.RemoteDictionaryServiceImpl;
import org.cmas.remote.RemoteLogbookService;
import org.cmas.remote.RemoteLogbookServiceImpl;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.remote.RemoteRegistrationServiceImpl;
import org.cmas.service.CodeService;
import org.cmas.service.CodeServiceImpl;
import org.cmas.service.DiverService;
import org.cmas.service.DiverServiceImpl;
import org.cmas.service.EntityDeleteService;
import org.cmas.service.LoginService;
import org.cmas.service.LoginServiceImpl;
import org.cmas.service.NavigationService;
import org.cmas.service.PushDispatcherService;
import org.cmas.service.RegistrationService;
import org.cmas.service.RegistrationServiceImpl;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.service.dictionary.DictionaryDataServiceImpl;
import org.cmas.service.divespot.DiveSpotService;
import org.cmas.service.divespot.DiveSpotServiceImpl;
import org.cmas.service.logbook.LogbookService;
import org.cmas.service.logbook.LogbookServiceImpl;
import org.cmas.service.social.DiverSearchService;
import org.cmas.service.social.DiverSearchServiceImpl;
import org.cmas.util.android.WakeLocker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings({
        "FieldAccessedSynchronizedAndUnsynchronized",
        "ClassWithTooManyFields",
        "ClassWithTooManyMethods",
        "OverlyCoupledClass"
})
public class BaseBeanContainer {

    private static final Object MONITOR = new Object();
    private static BaseBeanContainer instance;

    private AppProperties appProperties;

    private RemoteRegistrationServiceImpl remoteRegistrationService;
    private RemoteDictionaryServiceImpl remoteDictionaryService;

    private RegistrationServiceImpl registrationService;
    private LoginServiceImpl loginService;
    private DiverServiceImpl diverService;
    private CodeServiceImpl codeService;

    private DictionaryDataServiceImpl dictionaryDataService;

    private SettingsService settingsService;

    private EntityDeleteService entityDeleteService;
    private PushDispatcherService pushDispatcherService;
    private NavigationService navigationService;

    private WakeLocker wakeLocker;

    private PermissionChecker permissionChecker;

    private RemoteLogbookServiceImpl remoteDocumentService;
    private LogbookServiceImpl logbookService;
    private DiveSpotServiceImpl diveSpotService;
    private DiverSearchService diverSearchService;

    private CountryDao countryDao;

    private DiverDao diverDao;

    private LogbookEntryDaoImpl logbookEntryDao;
    private DiveSpotDaoImpl diveSpotDao;

    private List<CreateTableDao> allDaos;

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
        try {
            InputStream propStream = getClass().getResourceAsStream("app.properties");
            try {
                Properties props = new Properties();
                props.load(propStream);

                appProperties = new AppProperties(props);
            } finally {
                propStream.close();
            }

            remoteRegistrationService = new RemoteRegistrationServiceImpl();
            remoteDictionaryService = new RemoteDictionaryServiceImpl();

            registrationService = new RegistrationServiceImpl();
            loginService = new LoginServiceImpl();
            diverService = new DiverServiceImpl();

            codeService = new CodeServiceImpl();

            dictionaryDataService = new DictionaryDataServiceImpl();

            settingsService = new SettingsServiceImpl();

            wakeLocker = new WakeLocker();

            remoteDocumentService = new RemoteLogbookServiceImpl();
            logbookService = new LogbookServiceImpl();
            diveSpotService = new DiveSpotServiceImpl();
            diverSearchService = new DiverSearchServiceImpl();

            countryDao = new CountryDaoImpl();

            diverDao = new DiverDaoImpl();

            logbookEntryDao = new LogbookEntryDaoImpl();
            diveSpotDao = new DiveSpotDaoImpl();

            allDaos = new ArrayList<>(5);
            allDaos.add(countryDao);
            allDaos.add(diverDao);
            allDaos.add(logbookEntryDao);
            allDaos.add(diveSpotDao);

        } catch (IOException e) {
            throw new IllegalStateException("Context failed to initialize", e);
        }
    }

    public void initialize() {

        logbookEntryDao.initialize();
        diveSpotDao.initialize();

        registrationService.initialize();
        loginService.initialize();

        diverService.initialize();

        codeService.initialize();
        dictionaryDataService.initialize();

        remoteRegistrationService.initialize();
        remoteDictionaryService.initialize();

        remoteDocumentService.initialize();
        logbookService.initialize();
        diveSpotService.initialize();
    }

    public PermissionChecker getPermissionChecker() {
        return permissionChecker;
    }

    public void setPermissionChecker(PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    public CountryDao getCountryDao() {
        return countryDao;
    }

    public DiveSpotDao getDiveSpotDao() {
        return diveSpotDao;
    }

    public RemoteDictionaryService getRemoteDictionaryService() {
        return remoteDictionaryService;
    }

    public CodeService getCodeService() {
        return codeService;
    }

    public WakeLocker getWakeLocker() {
        return wakeLocker;
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public DiverService getDiverService() {
        return diverService;
    }

    public DiverSearchService getDiverSearchService() {
        return diverSearchService;
    }

    public DiverDao getDiverDao() {
        return diverDao;
    }

    public LogbookEntryDao getLogbookEntryDao() {
        return logbookEntryDao;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public List<CreateTableDao> getAllDaos() {
        return allDaos;
    }

    public RemoteRegistrationService getRemoteRegistrationService() {
        return remoteRegistrationService;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public EntityDeleteService getEntityDeleteService() {
        return entityDeleteService;
    }

    public void setEntityDeleteService(EntityDeleteService entityDeleteService) {
        this.entityDeleteService = entityDeleteService;
    }

    public PushDispatcherService getPushDispatcherService() {
        return pushDispatcherService;
    }

    public void setPushDispatcherService(PushDispatcherService pushDispatcherService) {
        this.pushDispatcherService = pushDispatcherService;
    }

    public NavigationService getNavigationService() {
        return navigationService;
    }

    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public DictionaryDataService getDictionaryDataService() {
        return dictionaryDataService;
    }

    public RemoteLogbookService getRemoteDocumentService() {
        return remoteDocumentService;
    }

    public LogbookService getLogbookService() {
        return logbookService;
    }

    public DiveSpotService getDiveSpotService() {
        return diveSpotService;
    }
}

