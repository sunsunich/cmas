package org.cmas.service;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import org.cmas.mobile.R;
import org.cmas.remote.ErrorCodes;

public abstract class ReLoginAction<T> {

    protected LoginService loginService;

    protected ReLoginAction(LoginService loginService) {
        this.loginService = loginService;
    }

    protected abstract Pair<T, String> getRemoteResult() throws Exception;

    protected abstract Pair<T, String> nullResultHandler(String errorMessage);

    protected abstract void okResultHandler(T okResult) throws Exception;

    public Pair<T, String> doAction(Context context) {
        try {
            Pair<T, String> result = getRemoteResult();
            if (result.first == null && ErrorCodes.SESSION_EXPIRED.equals(result.second)) {
                loginService.reLoginUserOnServer(
                        context
                );
                result = getRemoteResult();
            }
            if (result.first == null) {
                return nullResultHandler(result.second);
            }
            okResultHandler(result.first);

            return result;
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
            return nullResultHandler(context.getString(R.string.error_connecting_to_server));
        }
    }
}
