package org.cmas;

import org.cmas.dao.CreateTableDao;
import org.cmas.dao.DiverDaoImpl;
import org.cmas.dao.UserDao;
import org.cmas.dao.dictionary.DocumentTypeDao;
import org.cmas.dao.dictionary.DocumentTypeDaoImpl;
import org.cmas.dao.doc.DocFileDao;
import org.cmas.dao.doc.DocFileDaoFileImpl;
import org.cmas.dao.doc.DocumentDao;
import org.cmas.dao.doc.DocumentDaoImpl;
import org.cmas.dao.logbook.LogbookEntryDaoImpl;
import org.cmas.entities.diver.Diver;
import org.cmas.remote.RemoteDictionaryService;
import org.cmas.remote.RemoteDictionaryServiceImpl;
import org.cmas.remote.RemoteLogbookService;
import org.cmas.remote.RemoteLogbookServiceImpl;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.remote.RemoteRegistrationServiceImpl;
import org.cmas.service.CodeService;
import org.cmas.service.CodeServiceImpl;
import org.cmas.service.EntityDeleteService;
import org.cmas.service.LoginService;
import org.cmas.service.LoginServiceImpl;
import org.cmas.service.NavigationService;
import org.cmas.service.PushDispatcherService;
import org.cmas.service.RegistrationService;
import org.cmas.service.RegistrationServiceImpl;
import org.cmas.service.UserService;
import org.cmas.service.UserServiceImpl;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.service.dictionary.DictionaryDataServiceImpl;
import org.cmas.service.logbook.LogbookService;
import org.cmas.service.logbook.LogbookServiceImpl;
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
    private UserServiceImpl userService;
    private CodeServiceImpl codeService;

    private DictionaryDataServiceImpl dictionaryDataService;

    private SettingsService settingsService;

    private EntityDeleteService entityDeleteService;
    private PushDispatcherService pushDispatcherService;
    private NavigationService navigationService;

    private WakeLocker wakeLocker;

    private RemoteLogbookServiceImpl remoteDocumentService;
    private LogbookServiceImpl logbookService;

    private UserDao<Diver> userDao;

    private LogbookEntryDaoImpl logbookEntryDao;
    private DocumentTypeDao documentTypeDao;
    private DocumentDaoImpl documentDao;
    private DocFileDaoFileImpl docFileDao;

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
            userService = new UserServiceImpl();

            codeService = new CodeServiceImpl();

            dictionaryDataService = new DictionaryDataServiceImpl();

            settingsService = new SettingsServiceImpl();

            wakeLocker = new WakeLocker();

            remoteDocumentService = new RemoteLogbookServiceImpl();
            logbookService = new LogbookServiceImpl();


            userDao = new DiverDaoImpl();

            logbookEntryDao = new LogbookEntryDaoImpl();

            documentDao = new DocumentDaoImpl();
            documentTypeDao = new DocumentTypeDaoImpl();
            docFileDao = new DocFileDaoFileImpl();

            allDaos = new ArrayList<>(5);
            allDaos.add(userDao);
            allDaos.add(logbookEntryDao);

            allDaos.add(documentTypeDao);
            allDaos.add(documentDao);

        } catch (IOException e) {
            throw new IllegalStateException("Context failed to initialize", e);
        }
    }

    public void initialize() {

        logbookEntryDao.initialize();
        documentDao.initialize();

        registrationService.initialize();
        loginService.initialize();

        userService.initialize();

        codeService.initialize();
        dictionaryDataService.initialize();

        remoteRegistrationService.initialize();
        remoteDictionaryService.initialize();

        remoteDocumentService.initialize();
        logbookService.initialize();
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

    public UserService getUserService() {
        return userService;
    }

    public UserDao<Diver> getUserDao() {
        return userDao;
    }

    public LogbookEntryDaoImpl getLogbookEntryDao() {
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

    public DocumentTypeDao getDocumentTypeDao() {
        return documentTypeDao;
    }

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    public RemoteLogbookService getRemoteDocumentService() {
        return remoteDocumentService;
    }

    public LogbookService getLogbookService() {
        return logbookService;
    }

    public DocFileDao getDocFileDao() {
        return docFileDao;
    }
}

