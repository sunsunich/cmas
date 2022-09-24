package org.cmas.android;

import org.cmas.BaseBeanContainer;
import org.cmas.android.dagger.ComponentManager;
import org.cmas.android.storage.MobileDatabase;
import org.cmas.app.AppProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemInitializer {

    private static final SystemInitializer OUR_INSTANCE = new SystemInitializer();

    private SystemInitializer() {
    }

    public static SystemInitializer getInstance() {
        return OUR_INSTANCE;
    }

    public void initialize() throws IOException {
        MainApplication appContext = MainApplication.getAppContext();
//        dagger or koin???
//        ComponentManager componentManager = ComponentManager.getInstance();
//        componentManager.initComponents(appContext);

        MobileDatabase.initialize(appContext);

        AppProperties appProperties;
        try (InputStream propStream = appContext.getAssets().open("app.properties")) {
            Properties props = new Properties();
            props.load(propStream);
            appProperties = new AppProperties(props);
        }



        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        beanContainer.setAppProperties(appProperties);
        beanContainer.initialize();
    }
}
