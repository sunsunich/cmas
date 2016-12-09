package org.cmas.service;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.mobile.R;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.UserDao;
import org.cmas.dao.dictionary.CountryDao;
import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.json.JsonBindingResultModel;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.util.ErrorCodeLocalizer;
import org.cmas.util.StringUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationServiceImpl implements RegistrationService, InitializingBean {

    private DiverService diverService;
    private UserDao<Diver> userDao;
    private CountryDao countryDao;
    private DictionaryDataService dictionaryDataService;
    private RemoteRegistrationService remoteRegistrationService;
    private LoginService loginService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        diverService = beanContainer.getDiverService();
        userDao = beanContainer.getDiverDao();
        countryDao = beanContainer.getCountryDao();

        remoteRegistrationService = beanContainer.getRemoteRegistrationService();
        loginService = beanContainer.getLoginService();
        dictionaryDataService = beanContainer.getDictionaryDataService();
    }

    @Override
    public String registerUser(Activity activity, String countryName, String firstName, String lastName, String dobStr) {
        Country country = dictionaryDataService.getByName(activity, countryDao, countryName);
        if(country == null){
            return activity.getString(R.string.invalid_country);
        }

        try {
            Globals.getDTF().parse(dobStr);
        } catch (ParseException ignored) {
            return activity.getString(R.string.invalid_dob);
        }

        try {
            Pair<JsonBindingResultModel, String> registerResult = remoteRegistrationService.checkDiverRegistration(
                    activity, country.getCode(), firstName, lastName, dobStr
            );
            if(registerResult.first ==null){
                return ErrorCodeLocalizer.getLocalMessage(
                        activity, registerResult.second, false
                );
            }
            if(registerResult.first.getSuccess()){
                return "";
            }
            Map<String, String> fieldErrors = registerResult.first.getFieldErrors();
            if(fieldErrors.isEmpty()){
                List<String> errors = registerResult.first.getErrors();
                if(errors.isEmpty()){
                    return activity.getString(R.string.error_connecting_to_server);
                }
                return ErrorCodeLocalizer.getLocalMessage(
                        activity, errors.get(0), false
                );
            }

            //todo better impl
            Map.Entry<String, String> someError = fieldErrors.entrySet().iterator().next();
            return someError.getKey() + ": " + ErrorCodeLocalizer.getLocalMessage(
                    activity, someError.getValue(), false
            );
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
}
