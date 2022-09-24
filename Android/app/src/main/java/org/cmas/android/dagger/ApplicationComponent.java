package org.cmas.android.dagger;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import org.cmas.android.MainApplication;
import org.cmas.android.ui.file.FileAdditionFragment;
import org.cmas.android.ui.signin.PostToServerService;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {MainModule.class}
)
public interface ApplicationComponent extends AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder applicationModule(MainModule applicationModule);

        ApplicationComponent build();
    }

    void inject(FileAdditionFragment fileAdditionFragment);

    void inject(PostToServerService postToServerService);
}
