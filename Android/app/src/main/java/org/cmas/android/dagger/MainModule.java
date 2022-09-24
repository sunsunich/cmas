package org.cmas.android.dagger;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import org.cmas.android.MainApplication;

@Module
public class MainModule {

    private final MainApplication mainApplication;

    public MainModule(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Provides
    public Context provideContext(){
        return mainApplication.getApplicationContext();
    }
}
