package org.cmas.android.ui.signin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.android.MainActivity;
import org.cmas.android.i18n.ErrorCodesManager;
import org.cmas.ecards.R;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.Base64Coder;

import javax.annotation.Nullable;
import java.io.File;
import java.security.SecureRandom;
import java.util.List;

public class PostToServerService extends LifecycleService {

    public static final String CHANNEL_ID = "PostToServerService";
    public static final String START_FOREGROUND_ACTION = "PostToServerServiceStart";
    public static final String STOP_FOREGROUND_ACTION = "PostToServerServiceStop";
    public static final String REGISTRATION_FORM_OBJECT_KEY = "RegistrationFormObject";
    public static final String REGISTRATION_FILE_PATH_KEY = "RegistrationFilePaths";
//    public static final String POST_TO_SERVICE_RESULT_KEY = "PostToServiceResult";

    private static final int POST_TO_SERVER_NOTIFICATION_ID = 1;

    private final ErrorCodesManager errorCodesManager = BaseBeanContainer.getInstance().getErrorCodesManager();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;

    private final SecureRandom random = new SecureRandom();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (inProgress()) {
            Log.w(getClass().getName(), "onStartCommand is called while background task is running");
            return START_NOT_STICKY;
        }
        String action = intent.getAction();
        if (STOP_FOREGROUND_ACTION.equals(action)) {
            stopForeground(true);
            stopSelfResult(startId);
        } else if (START_FOREGROUND_ACTION.equals(action)) {
            // indicate in progress state
            BaseBeanContainer.getInstance().getPostToServiceResultLiveData().postValue(null);
            RegistrationFormObject registrationFormObject =
                    (RegistrationFormObject) intent.getSerializableExtra(REGISTRATION_FORM_OBJECT_KEY);
            startForeground(POST_TO_SERVER_NOTIFICATION_ID,
                            getNotification(getString(R.string.upload_progress_header),
                                            getString(R.string.upload_progress_text),
                                            createDefaultNotificationIntent(registrationFormObject)
                            )
            );

            List<String> filePaths = intent.getStringArrayListExtra(REGISTRATION_FILE_PATH_KEY);
            disposable = Completable.fromAction(
                    () -> {Thread.sleep(20000L); reportResult(random.nextBoolean() ? "test error" : null, registrationFormObject, filePaths);}
                    //reportResult(runInBackground(registrationFormObject, filePaths))
            ).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();
            compositeDisposable.add(disposable);
        }
        return START_NOT_STICKY;
    }

    public boolean inProgress() {
        return disposable != null && !disposable.isDisposed();
    }


    private void reportResult(@Nullable String result, RegistrationFormObject registrationFormObject, List<String> filePaths) {
        String header;
        String notificationText;
        boolean isSuccess = result == null;
        if (isSuccess) {
            header = getString(R.string.upload_success_header);
            notificationText = getString(R.string.upload_success_text);
        } else {
            Log.e(getClass().getName(), "error code:" + result);
            header = getString(R.string.upload_failure_header);
            notificationText = errorCodesManager.getByCode(result);
        }
        PostToServiceResult postToServiceResult = new PostToServiceResult(isSuccess, result, filePaths);
        Intent notificationIntent = createDefaultNotificationIntent(registrationFormObject);
//        notificationIntent.putExtra(POST_TO_SERVICE_RESULT_KEY, postToServiceResult);
        Notification notification = getNotification(header, notificationText, notificationIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(POST_TO_SERVER_NOTIFICATION_ID, notification);

        BaseBeanContainer.getInstance().getPostToServiceResultLiveData().postValue(
                postToServiceResult
        );
    }

    private String runInBackground(RegistrationFormObject registrationFormObject, List<String> filePaths) {
        try {
            for (String path : filePaths) {
                byte[] bytes = FileUtils.readFileToByteArray(new File(path));
                registrationFormObject.images.add(
                        "data:image/png;base64," + String.valueOf(Base64Coder.encode(bytes))
                );
            }
            Pair<SimpleGsonResponse, String> response = BaseBeanContainer.getInstance()
                                                                         .getRemoteRegistrationService()
                                                                         .diverRegistration(registrationFormObject);
            SimpleGsonResponse result = response.getLeft();
            if (result == null) {
                return response.getRight();
            }
            if (result.isSuccess()) {
                return null;
            } else {
                return result.getMessage();
            }
        } catch (Throwable e) {
            Log.e(getClass().getName(), "error while diver registration", e);
            return ErrorCodes.ERROR;
        }
    }

    private Notification getNotification(String header,
                                         String notificationText,
                                         Intent notificationIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(header)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();
    }

    private Intent createDefaultNotificationIntent(RegistrationFormObject registrationFormObject) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(REGISTRATION_FORM_OBJECT_KEY, registrationFormObject);
        return intent;
    }
}
