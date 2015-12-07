package org.cmas.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import org.cmas.R;
import org.cmas.util.DialogUtils;
import org.cmas.util.ErrorCodeLocalizer;
import org.cmas.util.ProgressTask;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityManagementAction<T> {

    protected EntityManagementAction() {
    }

    protected abstract Pair<T, String> manageEntity();

    public void doAction(
            final SecureFragment secureFragment,
            final String actionName,
            Class<? extends BaseFragment> gotoAfterActionFragmentClass
    ) {
        doAction(secureFragment
                , actionName
                , secureFragment.getString(R.string.saved_successfully)
                , gotoAfterActionFragmentClass, new Bundle()
        );
    }

    public void doAction(
            final SecureFragment secureFragment,
            final String actionName,
            final String successMessage,
            Class<? extends BaseFragment> gotoAfterActionFragmentClass
    ) {
        doAction(secureFragment
                , actionName
                , successMessage
                , gotoAfterActionFragmentClass, new Bundle()
        );
    }

    public void doAction(
            final SecureFragment secureFragment,
            final String actionName,
            final String successMessage,
            final Class<? extends BaseFragment> gotoAfterActionFragmentClass,
            final Bundle bundle
    ) {
        final List<String> messages = new ArrayList<String>();
        final FragmentActivity activity = secureFragment.getActivity();
        DialogUtils.showLoaderDialog(
                activity.getSupportFragmentManager(),
                new ProgressTask<Object>() {
                    @Override
                    public Object doTask(OnPublishProgressListener listener) {
                        //todo добавить текстовое уведомления для того, что тут происходит
                        Pair<T, String> result = manageEntity();
                        if (result.first == null) {
                            messages.add(result.second);
                            return null;
                        }
                        return new Object();
                    }

                    @Override
                    public void doAfterTask(Object result) {
                        secureFragment.reportSaveSuccess(gotoAfterActionFragmentClass, bundle, successMessage);
                    }

                    @Override
                    public void handleError(String error) {
                        String message =
                                messages.isEmpty() ?
                                        activity.getString(R.string.error_connecting_to_server) :
                                        ErrorCodeLocalizer.getLocalMessage(activity, messages.get(0), false);
                        secureFragment.reportError(message);
                    }

                    @Override
                    public String getName() {
                        return actionName;
                    }
                }
        );
    }
}
