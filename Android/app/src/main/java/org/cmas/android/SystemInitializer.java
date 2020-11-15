package org.cmas.android;

import org.cmas.BaseBeanContainer;
import org.cmas.android.storage.MobileDatabase;

public class SystemInitializer {

    private static final SystemInitializer OUR_INSTANCE = new SystemInitializer();

    private SystemInitializer() {
    }

    public static SystemInitializer getInstance() {
        return OUR_INSTANCE;
    }

    public void initialize() {
        MobileDatabase.initialize(MainApplication.getAppContext());
        // todo dagger or koin?
        BaseBeanContainer.getInstance().initialize();
    }

    public void finalise() {
        MobileDatabase.finalise();
    }
}
