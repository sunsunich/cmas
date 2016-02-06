package org.cmas.util;

import android.content.Context;
import org.cmas.R;
import org.cmas.remote.ErrorCodes;

public class ErrorCodeLocalizer {

    public static String getLocalMessage(Context context, String code, boolean isInternal) {
        if (StringUtil.isTrimmedEmpty(code) || ErrorCodes.ERROR.equals(code)) {
            if (isInternal) {
                return context.getString(R.string.fatal_error);
            } else {
                return context.getString(R.string.error_connecting_to_server);
            }
        }
        if (ErrorCodes.PROFILE_ALREADY_EXISTS.equals(code)) {
            return context.getString(R.string.profile_already_exists);
        }

        if (ErrorCodes.USER_ALREADY_EXISTS.equals(code)) {
            return context.getString(R.string.user_already_exists);
        }

        if (ErrorCodes.NO_SUCH_USER.equals(code)) {
            return context.getString(R.string.no_such_user);
        }

        if (ErrorCodes.WRONG_PASSWORD.equals(code)) {
            return context.getString(R.string.wrong_password);
        }

        if (code.contains(ErrorCodes.ERROR_WHILE_SAVING_PROFILE)) {
            return context.getString(R.string.error_while_saving_profile);
        }

        if (code.contains(ErrorCodes.ERROR_WHILE_SAVING_DOCUMENT)) {
            return context.getString(R.string.error_while_saving_document);
        }

        if(code.contains(ErrorCodes.EMAIL_ALREADY_EXISTS)){
            return context.getString(R.string.email_already_exists);
        }

        /*
        no localized messages for these codes
         */
        if (code.contains(ErrorCodes.UNSUPPORTED_DEVICE_TYPE)) {
            return context.getString(R.string.error_connecting_to_server);
        }

        if (code.contains(ErrorCodes.ERROR_REGISTERING_DEVICE)) {
            return context.getString(R.string.error_connecting_to_server);
        }

        if (code.contains(ErrorCodes.ERROR_UNREGISTERING_DEVICE)) {
            return context.getString(R.string.error_connecting_to_server);
        }

        if (code.contains(ErrorCodes.ERROR_WHILE_SAVING_USER)) {
            return context.getString(R.string.error_connecting_to_server);
        }
        if(code.contains(ErrorCodes.INTERNET_IS_UNAVAILABLE)){
            return context.getString(R.string.internet_is_unavailable);
        }

        if(code.contains(ErrorCodes.USER_IS_DELETED)){
            return context.getString(R.string.user_is_deleted);
        }

        return code;
    }
}
