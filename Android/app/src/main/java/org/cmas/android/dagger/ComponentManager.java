/*
 * Copyright (C) Fluidic Analytics Limited, 2021.
 * All rights reserved.
 */

package org.cmas.android.dagger;

import org.cmas.android.MainApplication;

import javax.annotation.Nonnull;

public final class ComponentManager {

    private static ComponentManager INSTANCE = new ComponentManager();

    public static ComponentManager getInstance() {
        return INSTANCE;
    }

    private ApplicationComponent applicationComponent;

    public void initComponents(MainApplication application) {
        applicationComponent = DaggerApplicationComponent.builder()
                                                         .application(application)
                                                         .applicationModule(new MainModule(application))
                                                         .build();
    }

    @Nonnull
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
