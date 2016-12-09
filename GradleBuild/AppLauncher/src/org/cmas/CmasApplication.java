package org.cmas;

import android.app.Application;

/**
 * User: ABadretdinov
 * Date: 20.03.14
 * Time: 18:54
 */
public class CmasApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        ImageLoader.getInstance().init(config);
    }
}
