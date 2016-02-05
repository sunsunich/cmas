package org.cmas;

import org.cmas.service.EntityDeleteServiceImpl;
import org.cmas.service.NavigationService;
import org.cmas.service.NavigationServiceImpl;
import org.cmas.service.PushDispatcherServiceImpl;

@SuppressWarnings({
        "FieldAccessedSynchronizedAndUnsynchronized",
        "ClassWithTooManyFields",
        "ClassWithTooManyMethods",
        "OverlyCoupledClass"
})
public class BeanInitializer {

    private static final Object MONITOR = new Object();
    private static BeanInitializer instance = null;

    public static BeanInitializer getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (MONITOR) {
            if (instance == null) {
                instance = new BeanInitializer();
            }
        }
        return instance;
    }

    private BeanInitializer() {
        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
        NavigationService navigationService = new NavigationServiceImpl();
        baseBeanContainer.setNavigationService(navigationService);

        EntityDeleteServiceImpl entityDeleteService = new EntityDeleteServiceImpl();
        entityDeleteService.initialize();
        baseBeanContainer.setEntityDeleteService(entityDeleteService);

        PermissionChecker permissionChecker = new PermissionCheckerImpl();
        baseBeanContainer.setPermissionChecker(permissionChecker);

        PushDispatcherServiceImpl dispatcherService = new PushDispatcherServiceImpl();
        dispatcherService.initialize();
        baseBeanContainer.setPushDispatcherService(dispatcherService);

        baseBeanContainer.initialize();

    }
}
