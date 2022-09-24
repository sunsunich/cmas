package org.cmas.remote;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonSyntaxException;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.app.AppProperties;
import org.cmas.app.Settings;
import org.cmas.app.SettingsService;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.remote.json.DateDeserializer;
import org.cmas.util.StringUtil;
import org.cmas.util.http.SimpleHttpsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRemoteServiceImpl.class);

    protected static final String SESSION_COOKIE_NAME = "JSESSIONID";   //PHPSESSID on HB-32

    protected SettingsService settingsService;
    protected AppProperties appProperties;
    protected NetworkManager networkManager;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        settingsService = beanContainer.getSettingsService();
        appProperties = beanContainer.getAppProperties();
        networkManager = beanContainer.getNetworkManager();
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicGetRequestSend(
            String url
            , Map<String, Object> params
            , Type resultType
    )
            throws Exception {
        return basicGetRequestSend(url, params, resultType, Globals.DTF);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicGetRequestSend(
            String url
            , Map<String, Object> params
            , Type resultType
            , String dateFormat
    )
            throws Exception {
        return basicRequestSend(url, params, resultType, "GET", dateFormat);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicPostRequestSend(
            String url
            , Map<String, Object> params
            , Type resultType
    )
            throws Exception {
        return basicPostRequestSend(url, params, resultType, Globals.DTF);
    }

    protected <T> Pair<Pair<T, String>, Map<String, String>> basicPostRequestSend(
            String url
            , Map<String, Object> params
            , Type resultType
            , String dateFormat
    )
            throws Exception {
        return basicRequestSend(url, params, resultType, "POST", dateFormat);
    }

    private <T> Pair<Pair<T, String>, Map<String, String>> basicRequestSend(
            String url, Map<String, Object> params, Type resultType
            , String requestMethod
            , String dateFormat
    ) throws Exception {
        if (!networkManager.isNetworkAvailable()) {
            throw new NetworkUnavailableException("Network is unavailable");
        }

        String serverUrl = appProperties.getServerHOST() + url;
        Settings settings = settingsService.getSettings();
        Map<String, String> cookies = getSessionCookie(settings);

        //  AliasKeyManager keyManager = networkManager.getAliasKeyManager();
        Pair<String, Map<String, String>> reply = SimpleHttpsClient.sendRequest(
                serverUrl,
                //      keyManager.getKeyStore(),
                Globals.UTF_8_ENC, params, cookies, requestMethod
        );
        String responseBody = reply.getLeft();
        if (resultType.equals(String.class)) {
            return Pair.of(Pair.of((T)responseBody, ""), reply.getRight());
        }
        T result;
        String message;
        try {
            try {
                Gson simpleGson = new Gson();
                SimpleGsonResponse simpleGsonResponse = simpleGson.fromJson(responseBody, SimpleGsonResponse.class);
                message = simpleGsonResponse.getMessage();
                Boolean success = simpleGsonResponse.isSuccess();
                if (success != null && !success) {
                    return Pair.of(
                            Pair.<T, String>of(null, message), reply.getRight()
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
            LOGGER.warn("Warn. Error message from server: " + responseBody, e);
            result = null;
            Gson gson = new Gson();
            message = gson.fromJson(responseBody, SimpleGsonResponse.class).getMessage();
        }
        return Pair.of(Pair.of(result, message), reply.getRight());
    }

    protected Map<String, String> getSessionCookie(Settings settings) {
        String jsessionid = settings.getJsessionid();
        Map<String, String> cookies = new HashMap<>();
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
