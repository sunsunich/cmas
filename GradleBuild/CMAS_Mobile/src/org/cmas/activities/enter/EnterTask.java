package org.cmas.activities.enter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.entities.diver.Diver;
import org.cmas.mobile.R;
import org.cmas.service.LoginService;
import org.cmas.service.NavigationService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;

import java.util.ArrayList;
import java.util.List;

public abstract class EnterTask {

    private final String name;
    private final String username;
    private final String password;
    private FragmentActivity activity;
    private final BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
    private final LoginService loginService = beanContainer.getLoginService();
    private final NavigationService navigationService = beanContainer.getNavigationService();

    protected EnterTask(String name, String username, String password, FragmentActivity activity) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    public void execute(final Bundle extras){
        final List<String> messages = new ArrayList<String>();
        DialogUtils.showLoaderDialog(activity.getSupportFragmentManager(), new ProgressTask<String>() {
            @Override
            public String doTask(OnPublishProgressListener listener) {
                String message = prepareEnterTask();
                if (message.isEmpty()) {
                    listener.onPublishProgress(activity.getString(R.string.authorization_attempt));
                    Pair<Diver, String> loginResult = loginService.loginUser(activity, username, password);
                    if (loginResult.first == null) {
                        messages.add(loginResult.second);
                        return null;
                    } else {
                        return "";
                    }
                } else {
                    messages.add(message);
                    return null;
                }
            }

            @Override
            public void doAfterTask(String result) {
                Intent intent = new Intent(
                        activity
                        , navigationService.getMainActivity()
                );
                if(extras!=null)
                {
                    intent.putExtra("bundle",extras.getBundle("bundle"));
                }
                activity.startActivity(intent);
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
                activity = null;
            }

            @Override
            public void handleError(String error) {
                if (messages.isEmpty()) {
                    EnterTask.this.handleError(error);
                } else {
                    EnterTask.this.handleError(messages.get(0));
                }
                activity = null;
            }

            @Override
            public String getName() {
                return name;
            }
        }, name);
    }

    protected abstract void handleError(String message);

    protected abstract String prepareEnterTask();
}
