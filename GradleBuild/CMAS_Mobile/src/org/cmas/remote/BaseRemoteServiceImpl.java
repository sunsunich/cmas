package org.cmas.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Pair;
import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonSyntaxException;
import org.cmas.AppProperties;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.mobile.R;
import org.cmas.remote.json.DateDeserializer;
import org.cmas.util.StringUtil;
import org.cmas.util.android.SecurePreferences;
import org.cmas.util.http.SimpleHttpClient;
import org.cmas.util.http.ssl.AliasKeyManager;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 20/11/12
 * Time: 18:48
 */
public abstract class BaseRemoteServiceImpl implements BaseRemoteService, InitializingBean {

    private static final String KEYSTORE_PASS = "oV^h6g1i^sL1";
    private static final String KEYSTORE_ALIAS = "cmas";

    protected static final String SESSION_COOKIE_NAME = "JSESSIONID";   //PHPSESSID on HB-32

    protected SettingsService settingsService;
    protected AppProperties appProperties;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        settingsService = beanContainer.getSettingsService();
        appProperties = beanContainer.getAppProperties();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicGetRequestSend(
            Context context, String url
            , Map<String, Object> params
            , Type resultType
    )
            throws Exception {
        return basicGetRequestSend(context, url, params, resultType, Globals.DTF);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicGetRequestSend(
            Context context
            , String url
            , Map<String, Object> params
            , Type resultType
            , String dateFormat
    )
            throws Exception {
        return basicRequestSend(context, url, params, resultType, "GET", dateFormat);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicPostRequestSend(
            Context context, String url
            , Map<String, Object> params
            , Type resultType
    )
            throws Exception {
        return basicPostRequestSend(context, url, params, resultType, Globals.DTF);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicPostRequestSend(
            Context context, String url
            , Map<String, Object> params
            , Type resultType
            , String dateFormat
    )
            throws Exception {
        return basicRequestSend(context, url, params, resultType, "POST", dateFormat);
    }

    private <T> Pair<Pair<T, String>, Map<String, String>> basicRequestSend(
            Context context, String url, Map<String, Object> params, Type resultType
            , String requestMethod
            , String dateFormat
    ) throws Exception {
        if (!isNetworkAvailable(context)) {
            throw new NetworkUnavailableException("Network is unavailable");
        }

        String serverUrl = appProperties.getServerHOST() + url;
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        Settings settings = settingsService.getSettings(sharedPreferences);
        Map<String, String> cookies = getSessionCookie(settings);

        AliasKeyManager keyManager;
        InputStream stream = context.getResources().openRawResource(R.raw.android);
        try {
            String keyStorePass = KEYSTORE_PASS;
            String keyStoreAlias = KEYSTORE_ALIAS;
            keyManager = new AliasKeyManager(
                    stream
                    , keyStorePass.toCharArray()
                    , keyStoreAlias
            );
        } finally {
            stream.close();
        }

        Pair<String, Map<String, String>> reply = SimpleHttpClient.sendRequest(
                serverUrl,
                keyManager.getKeyStore(),
                Globals.UTF_8_ENC, params, cookies, requestMethod
        );
        String responseBody = reply.first;
        T result;
        String message;
        try {
            try {
                Gson simpleGson = new Gson();
                SimpleGsonResponse simpleGsonResponse = simpleGson.fromJson(responseBody, SimpleGsonResponse.class);
                message = simpleGsonResponse.getMessage();
                Boolean success = simpleGsonResponse.isSuccess();
                if (success != null && !success) {
                    return new Pair<Pair<T, String>, Map<String, String>>(
                            new Pair<T, String>(null, message), reply.second
                    );
                }
            } catch (JsonSyntaxException ignored) {
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            Date.class, new DateDeserializer(dateFormat))
                    .create();
            result = gson.fromJson(responseBody, resultType);
            message = "";

        } catch (JsonSyntaxException e) {
            Log.w(getClass().getName(), "Warn. Error message from server: " + responseBody, e);
            result = null;
            Gson gson = new Gson();
            message = gson.fromJson(responseBody, SimpleGsonResponse.class).getMessage();
        }
        return new Pair<>(new Pair<>(result, message), reply.second);
    }

    protected Map<String, String> getSessionCookie(Settings settings) {
        String jsessionid = settings.getJsessionid();
        Map<String, String> cookies = new HashMap<String, String>();
        if (!StringUtil.isTrimmedEmpty(jsessionid)) {
            cookies.put(SESSION_COOKIE_NAME, jsessionid);
        }
        return cookies;
    }

    public static void main(String[] args) {
//        try {
//            Gson gson = new GsonBuilder()
//                    .setDateFormat(Globals.DTF)
//                    .create();
//            Type resultType = new TypeToken<List<ProfileDisplayModel>>() {
//            }.getType();
//            String responseBody = "[{\"user\":{\"id\":124,\"relationship\":\"\",\"version\":1989,\"extId\":3720454252014032,\"sex\":\"M\",\"dob\":\"01.03.1972\",\"oms\":\"11111\",\"snils\":\"\",\"name\":\"Леонард\",\"default\":true" +
//                    ",\"userpic\":\"....\"},\"newDirectivesCnt\":0}," +
//                    "{\"user\":{\"id\":488,\"relationship\":\"\",\"version\":1979,\"extId\":4558098732014041,\"sex\":\"M\",\"oms\":\"OMS-123456\",\"snils\":\"\",\"name\":\"Бабушка\",\"default\":false,\"userpic\":\"\"},\"newDirectivesCnt\":0}]";
//
//            List<ProfileDisplayModel> result =
//                    gson.fromJson(responseBody, resultType);
//            System.out.println();
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }
    }
}
