package org.cmas.util.http;

import com.google.firebase.database.utilities.Pair;
import org.cmas.Globals;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleHttpsClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpsClient.class);

    private static final char COOKIE_NAME_VAL_SEPARATOR = '=';
    private static final char COOKIE_ATTR_SEPARATOR = ';';

    private SimpleHttpsClient() {
    }

    public static Pair<String, Map<String, String>> sendGetRequest(
            String url
            , KeyStore keyStore
            , String encoding
            , Map<String, Object> params
            //nullable
            , Map<String, String> cookies,
            boolean isJson
    ) throws Exception {
        return sendRequest(url, keyStore, encoding, params, cookies, null, null, "GET", isJson);
    }

    public static Pair<String, Map<String, String>> sendPostRequest(
            String url,
            String postBody,
            String basicUsername,
            String basicPassword
    ) throws Exception {
        Map<String, Object> params = new HashMap<>(1);
        params.put("", postBody);
        return sendPostRequest(url, null, Globals.UTF_8_ENC, params, null, basicUsername, basicPassword, true);
    }

    public static Pair<String, Map<String, String>> sendPostRequest(
            String url
            , KeyStore keyStore
            , String encoding
            , Map<String, Object> params
            //nullable
            , Map<String, String> cookies,
            //nullable
            String basicUsername,
            //nullable
            String basicPassword,
            boolean isJson
    ) throws Exception {
        return sendRequest(url, keyStore, encoding, params, cookies, basicUsername, basicPassword, "POST", isJson);
    }

    private static Pair<String, Map<String, String>> sendRequest(
            String url,
            KeyStore keyStore,
            String encoding,
            Map<String, Object> params,
            Map<String, String> cookies,
            String basicUsername,
            String basicPassword,
            String requestMethod,
            boolean isJson
    ) throws Exception {
        String body;
        if (isJson) {
            body = (String) params.values().iterator().next();
        } else {
            StringBuilder bodyBuilder = new StringBuilder();
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Map.Entry<String, Object> param = iterator.next();
                Object obj = param.getValue();
                String key = param.getKey();
                if (obj instanceof List) {
                    List list = (List) obj;
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        String value = String.valueOf(list.get(i));
                        value = value == null ? "" : value;
                        bodyBuilder.append(key).append('=')
                                   .append(URLEncoder.encode(value, encoding));
                        if (i != size - 1) {
                            bodyBuilder.append('&');
                        }
                    }
                } else {
                    String value = (String) obj;
                    value = value == null ? "" : value;
                    bodyBuilder.append(key).append('=');
                    bodyBuilder.append(URLEncoder.encode(value, encoding));
                }
                if (iterator.hasNext()) {
                    bodyBuilder.append('&');
                }
            }
            body = bodyBuilder.toString();
        }
        URL urlObj;
        try {
            if ("POST".equals(requestMethod)) {
                urlObj = new URL(url);
            } else {
                urlObj = new URL(url + '?' + body);
            }
        } catch (MalformedURLException e) {
            LOG.error("URL error: " + e.getMessage(), e);
            throw e;
        }

        LOG.error("Sending http " + requestMethod + " request to " + url);
        LOG.error("Http body: " + body);

        HttpsURLConnection urlConnection = (HttpsURLConnection) urlObj.openConnection();

        //TODO fix this when we have a proper server with proper host name
        // http://developer.android.com/training/articles/security-ssl.html#CommonHostnameProbs
        urlConnection.setHostnameVerifier(
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }
        );
        if (basicUsername != null && basicPassword != null) {
            String credentials = Base64Coder.encodeString(basicUsername + ":" + basicPassword);
            urlConnection.setRequestProperty("Authorization", "Basic " + credentials);
        }
        if (keyStore != null) {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
        }

        urlConnection.setRequestProperty("Accept-Charset", encoding);
        if (cookies != null) {
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                urlConnection.addRequestProperty(
                        "Cookie",
                        cookie.getKey() + COOKIE_NAME_VAL_SEPARATOR + cookie.getValue()
                );
                // .append("Path=/; Domain=");
            }
        }

        urlConnection.setRequestMethod(requestMethod);
        return sendHttpRequest(encoding, requestMethod, isJson, urlObj, body, urlConnection);
    }

    private static void writePostBody(String encoding, boolean isJson, String paramEntity, HttpsURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);
        byte[] bytes = paramEntity.getBytes(encoding);
        if (isJson) {
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            urlConnection.setFixedLengthStreamingMode(bytes.length);
            writeStream(urlConnection.getOutputStream(), bytes);
        } else {
            urlConnection.setChunkedStreamingMode(0);
            writeStream(urlConnection.getOutputStream(), paramEntity, encoding);
        }
    }

    @NotNull
    private static Pair<String, Map<String, String>> sendHttpRequest(String encoding, String requestMethod, boolean isJson, URL urlObj, String body, HttpsURLConnection urlConnection) throws Exception {
        try {
            if ("POST".equals(requestMethod)) {
                //            urlConnection.setReadTimeout(7000);
                //            urlConnection.setConnectTimeout(7000);
                writePostBody(encoding, isJson, body, urlConnection);
            }

            urlConnection.connect();

            String responseBody = readStream(urlConnection.getInputStream(), encoding);

            String actualHost = urlConnection.getURL().getHost();
            if (!urlObj.getHost().equals(actualHost)) {
                String message = "Unexpected redirect to: " + actualHost;
                LOG.error(message);
                throw new IllegalStateException(message);
            }
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String errorTxt = readStream(urlConnection.getErrorStream(), encoding);
                String message = "Method failed: " + errorTxt;
                LOG.error(message);
                throw new IllegalStateException(message);
            }

            List<String> cookiePairs = urlConnection.getHeaderFields().get("Set-Cookie");
            if (cookiePairs == null) {
                return new Pair<String, Map<String, String>>(responseBody, new HashMap<String, String>());
            } else {
                Map<String, String> cookieMap = new HashMap<>(cookiePairs.size());
                for (String cookiePair : cookiePairs) {
                    int firstAttrSeparator = cookiePair.indexOf(COOKIE_NAME_VAL_SEPARATOR);
                    String cookieName = cookiePair.substring(0, firstAttrSeparator);
                    String cookieVal = cookiePair.substring(
                            firstAttrSeparator + 1, cookiePair.indexOf(COOKIE_ATTR_SEPARATOR)
                    );
                    cookieMap.put(cookieName, cookieVal);
                }

                return new Pair<>(responseBody, cookieMap);
            }

        } catch (Exception e) {
            String message = "Fatal transport error: " + e.getMessage();
            LOG.error(message, e);
            String errorMessage = readStream(urlConnection.getErrorStream(), encoding);
            if (StringUtil.isTrimmedEmpty(errorMessage)) {
                throw e;
            } else {
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    return new Pair<>(errorMessage, null);
                }
                throw new Exception(errorMessage, e);
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Pair<String, Map<String, String>> sendPostRequestForEntity(String url,
                                                                             KeyStore keyStore,
                                                                             String encoding,
                                                                             String paramEntity,
                                                                             Map<String, String> cookies) throws Exception {
        return sendPostRequestForEntity(url, keyStore, encoding, false, paramEntity, cookies);
    }

    public static Pair<String, Map<String, String>> sendPostRequestForEntity(String url,
                                                                             KeyStore keyStore,
                                                                             String encoding,
                                                                             boolean isJson,
                                                                             String paramEntity,
                                                                             Map<String, String> cookies) throws Exception {
        URL urlObj = new URL(url);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        HttpsURLConnection urlConnection = (HttpsURLConnection) urlObj.openConnection();

        //TODO fix this when we have a proper server with proper host name
        // http://developer.android.com/training/articles/security-ssl.html#CommonHostnameProbs
        urlConnection.setHostnameVerifier(
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }
        );

        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setRequestProperty("Accept-Charset", encoding);

        if (cookies != null) {
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                urlConnection.addRequestProperty(
                        "Cookie",
                        cookie.getKey() + COOKIE_NAME_VAL_SEPARATOR + cookie.getValue()
                );
                // .append("Path=/; Domain=");
            }
        }
        //            urlConnection.setReadTimeout(7000);
        //            urlConnection.setConnectTimeout(7000);
        urlConnection.setRequestMethod("POST");
        return sendHttpRequest(encoding, "POST", isJson, urlObj, paramEntity, urlConnection);
    }

    private static String readStream(InputStream in, String encoding) throws IOException {
        if (in == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    private static void writeStream(OutputStream outputStream, byte[] body) throws IOException {
        outputStream.write(body);
    }

    private static void writeStream(OutputStream outputStream, String body, String encoding) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, encoding))) {
            writer.write(body);
        }
    }

}
