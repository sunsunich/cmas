package org.cmas.service;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.R;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.UserDao;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.json.user.RegisterNewUserReply;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.util.ErrorCodeLocalizer;
import org.cmas.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationServiceImpl implements RegistrationService, InitializingBean {

    private UserService userService;
    private UserDao<Diver> userDao;

    private RemoteRegistrationService remoteRegistrationService;
    private LoginService loginService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        userService = beanContainer.getUserService();
        userDao = beanContainer.getUserDao();

        remoteRegistrationService = beanContainer.getRemoteRegistrationService();
        loginService = beanContainer.getLoginService();
    }

    @Override
    public String registerUser(Activity activity, String email, String password, String repeatPassword) {
        String message = validateRegisterUser(activity, email, password, repeatPassword);
        if (!message.isEmpty()) {
            return message;
        }
        try {
            Pair<RegisterNewUserReply, String> registerResult = remoteRegistrationService.registerUsername(
                    activity, email, password
            );
            RegisterNewUserReply mainResult = registerResult.first;
            if (mainResult == null) {
                return ErrorCodeLocalizer.getLocalMessage(activity, registerResult.second, false);
            }
            Long userId = mainResult.getId();
            if (userId == null) {
                return registerResult.second;
            } else {
                Diver newUser = new Diver();
                newUser.setId(userId);
                newUser.setEmail(email);
                newUser.setPassword(password);
                DataBaseHolder dataBaseHolder = new DataBaseHolder(activity);
                SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {
                    userDao.save(writableDatabase, newUser);
                    return "";
                } finally {
                    writableDatabase.close();
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
            return activity.getString(R.string.error_connecting_to_server);
        }
    }

    @Override
    public String addCode(final Activity activity,
                          final String email,
                          final String code,
                          final String repeatCode
    ) {
        String message = validateAddCode(activity, code, repeatCode);
        if (!message.isEmpty()) {
            return message;
        }
        ReLoginAction<SimpleGsonResponse> reLoginAction = new ReLoginAction<SimpleGsonResponse>(loginService) {
            @Override
            protected Pair<SimpleGsonResponse, String> getRemoteResult() throws Exception {
                return remoteRegistrationService.addCode(
                        activity, code
                );
            }

            @Override
            protected Pair<SimpleGsonResponse, String> nullResultHandler(String errorMessage) {
                return new Pair<>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(SimpleGsonResponse result) {
                if (result.isSuccess()) {
                    DataBaseHolder dataBaseHolder = new DataBaseHolder(activity);
                    SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                    try {
                        Diver user = (Diver)userDao.getByEmail(writableDatabase, email);
                        user.setMobileLockCode(code);
                        userDao.update(writableDatabase, user);
                    } finally {
                        writableDatabase.close();
                    }
                }
            }
        };

        Pair<SimpleGsonResponse, String> registerResult = reLoginAction.doAction(activity);
        SimpleGsonResponse result = registerResult.first;
        if (result == null) {
            return ErrorCodeLocalizer.getLocalMessage(
                    activity, registerResult.second, false);
        } else {
            if (result.isSuccess()) {
                return "";
            } else {
                return ErrorCodeLocalizer.getLocalMessage(
                        activity, result.getMessage(), false);
            }
        }
    }

    private static final Pattern CODE_PATTERN = Pattern.compile("\\d{4}");

    protected String validateAddCode(Activity activity, String code, String repeatCode) {
        boolean isCodeEmpty = StringUtil.isTrimmedEmpty(code) && StringUtil.isTrimmedEmpty(repeatCode);
        if (!isCodeEmpty) {
            if (!repeatCode.equals(code)) {
                return activity.getString(R.string.code_mismatch_error);
            }
            Matcher matcher = CODE_PATTERN.matcher(code);
            if (!matcher.matches()) {
                return activity.getString(R.string.code_error);
            }
        }

        return "";
    }

    private String validateRegisterUser(Activity activity, String username, String password, String repeatPassword) {

        if (!repeatPassword.equals(password)) {
            return activity.getString(R.string.password_mismatch_error);
        }

        try {
            User user = userService.getByEmail(activity, username);
            if (user != null) {
                return activity.getString(R.string.user_already_exists);
            }
        } catch (Exception e) {
            Log.e(RegistrationServiceImpl.class.getName(), e.getMessage(), e);
            return activity.getString(R.string.fatal_error);
        }
        return "";
    }
}
