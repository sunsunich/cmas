package org.cmas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.DataBaseHolder;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.util.ProgressTask;
import org.cmas.util.StringUtil;
import org.cmas.util.android.DeviceUuidFactory;
import org.cmas.util.android.SecurePreferences;

import java.io.File;

public class AppLoader {

    private AppLoader() {
    }

    private static volatile boolean isDynamicLoaded = false;
    private static final Object DYNAMIC_MONITOR = new Object();

    private static volatile boolean isLoaded = false;
    private static final Object MONITOR = new Object();

    public static void loadApp(final Activity activity,ProgressTask.OnPublishProgressListener listener) {

        dynamicLoadApp(activity,listener);

        if (!isLoaded) {
            synchronized (MONITOR) {
                if (!isLoaded) {

                    BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
                    SettingsService settingsService = beanContainer.getSettingsService();

                    SharedPreferences sharedPreferences = new SecurePreferences(activity);
                    Settings settings = settingsService.getSettings(sharedPreferences);
                    if (StringUtil.isTrimmedEmpty(settings.getDeviceId())) {
                        settings.setDeviceId(
                                DeviceUuidFactory.getDeviceUUID(activity).toString()
                        );
                        settingsService.setSettings(sharedPreferences, settings);
                    }

//                    if (!GCMRegistrar.isRegisteredOnServer(activity)) {
//                        try {
//                            beanContainer.getRemoteRegistrationService().registerDevice(activity, registrationId);
//                        } catch (Exception e) {
//                            //TODO fatal error handling
//                            Log.e(AppLoader.class.getName(), e.getMessage(), e);
//                        }
//                    }


                    loadInitialData(activity,listener);
                    isLoaded = true;
                }
            }
        }

    }

    public static void dynamicLoadApp(Activity activity,ProgressTask.OnPublishProgressListener listener) {
        if (!isDynamicLoaded) {
            synchronized (DYNAMIC_MONITOR) {
                if (!isDynamicLoaded) {
                    BeanInitializer beanInitializer = BeanInitializer.getInstance();

                    disableConnectionReuseIfNecessary();
                    File appDir = new File(
                            DataBaseHolder.getDBLocation(activity)
                    );
                    if(listener!=null){
                        listener.onPublishProgress(activity.getString(R.string.loading_libraries));
                    }
                    SQLiteDatabase.loadLibs(activity, appDir);

                    DataBaseHolder dataBaseHolder = new DataBaseHolder(activity);
                    SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                    writableDatabase.close();
                    BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();

                    //TODO check internet connections

                    AppProperties appProperties = beanContainer.getAppProperties();
                    if (appProperties.isDebug()) {
//                        GCMRegistrar.checkDevice(activity);
//                        GCMRegistrar.checkManifest(activity);
                    }

//                    String registrationId = GCMRegistrar.getRegistrationId(activity);
//                    if (StringUtil.isTrimmedEmpty(registrationId)) {
//                        GCMRegistrar.register(activity, appProperties.getGcmSenderId());
//                    } else {
//                        SettingsService settingsService=beanContainer.getSettingsService();
//                        SharedPreferences sharedPreferences = new SecurePreferences(activity);
//                        Settings settings = settingsService.getSettings(sharedPreferences);
//                        settings.setGcmRegistrationId(registrationId);
//                        settingsService.setSettings(sharedPreferences, settings);
//                        Log.v(AppLoader.class.getName(), "Already registered");
//                    }
                    isDynamicLoaded = true;
                    if(listener!=null){
                        listener.onPublishProgress(activity.getString(R.string.loading_libraries_complete),"5");
                    }
                }
            }
        }
    }

    private static void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static void loadInitialData(Context context,ProgressTask.OnPublishProgressListener listener) {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        DictionaryDataService dictionaryDataService = beanContainer.getDictionaryDataService();

        // каждому методу передается maxProgress - общий прогресс, который на него выделен. в зависимости от него метод считает сколько процентов он выполнил
        dictionaryDataService.loadDictionaryEntities(context,listener,100);

        /*PortalBeanContainer portalBeanContainer=PortalBeanContainer.getInstance();
        ClinicService clinicService=portalBeanContainer.getClinicService();
        //todo для более детального обновления нужно передавать туда listener, либо загрузку реализовать здесь по частям. оба варианта не очень
        if(clinicService.needToUpdate(context)){
            clinicService.loadClinicsWithAddresses(context,listener,70);
        }*/
    }
//
//    private static void addUser(Context context, String userName) throws Exception {
//        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
//        RemoteRegistrationService remoteRegistrationService = beanContainer.getRemoteRegistrationService();
//        ProfileService profileService = beanContainer.getProfileService();
//        UserService userService = beanContainer.getUserService();
//        User user = userService.getByUsername(context, userName);
//        if(user == null){
//            user = new User();
//            user.setUsername(userName);
//            user.setPassword("aaa");
//            long userId = userService.persistUser(context, user);
//            user.setId(userId);
//            List<Profile> profiles = remoteRegistrationService.getProfiles(userName);
//            for(Profile profile : profiles){
//                profile.setUserId(userId);
//                profileService.saveNewProfile(context, profile);
//            }
//        }
//    }
}
