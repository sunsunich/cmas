package org.cmas.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface PushDispatcherService {

    Intent handlePush(Activity activity, Intent intent);

    void generateNotification(Context context, Intent intent);
}
