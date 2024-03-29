package org.cmas.util.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpsClient.class);

    public static final char COOKIE_NAME_VAL_SEPARATOR = '=';
    public static final char COOKIE_ATTR_SEPARATOR = ';';

    private SimpleHttpsClient() {
    }

    public static Pair<String, Map<String, String>> sendGetRequest(
            String url
        //    , KeyStore keyStore
            , String encoding
            , Map<String, Object> params
            , @Nullable Map<String, String> cookies
    ) throws Exception {
        return sendRequest(url, /*keyStore,*/ encoding, params, cookies, "GET");
    }

    public static Pair<String, Map<String, String>> sendPostRequest(
            String url
         //   , KeyStore keyStore
            , String encoding
            , Map<String, Object> params
            , @Nullable Map<String, String> cookies
    ) throws Exception {
        return sendRequest(url, /*keyStore,*/ encoding, params, cookies, "POST");
    }

    public static Pair<String, Map<String, String>> sendRequest(
            String url,
    //        KeyStore keyStore,
            String encoding,
            Map<String, Object> params,
            Map<String, String> cookies,
            String requestMethod
    ) throws Exception {
        URL urlObj;
        StringBuilder bodyBuilder = new StringBuilder();
        try {
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Map.Entry<String, Object> param = iterator.next();
                Object obj = param.getValue();
                if (obj instanceof List) {
                    List list = (List) obj;
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        String value = String.valueOf(list.get(i));
                        value = value == null ? "" : value;
                        bodyBuilder.append(param.getKey()).append('=')
                                   .append(URLEncoder.encode(value, encoding));
                        if (i != size - 1) {
                            bodyBuilder.append('&');
                        }
                    }
                } else {
                    String value = (String) obj;
                    value = value == null ? "" : value;
                    bodyBuilder.append(param.getKey()).append('=')
                               .append(URLEncoder.encode(value, encoding));
                }
                if (iterator.hasNext()) {
                    bodyBuilder.append('&');
                }
            }
            if ("POST".equals(requestMethod)) {
                urlObj = new URL(url);
            } else {
                urlObj = new URL(url + '?' + bodyBuilder.toString());
            }
        } catch (MalformedURLException e) {
            String message = "URL error: " + e.getMessage();
            LOGGER.error(message, e);
            throw e;
        }

   //     TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    //    tmf.init(keyStore);

    //    SSLContext context = SSLContext.getInstance("TLS");
      //  context.init(null, tmf.getTrustManagers(), null);

/* making ssl connection insecure, do not use in prod
        // Create a trust manager that does not validate certificate chains
//        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//            }
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//            }
//        }
//        };
//
//        // Install the all-trusting trust manager
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
*/
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

    //    urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setRequestProperty("Accept-Encoding", encoding);
        if (cookies != null) {
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                urlConnection.addRequestProperty(
                        "Cookie",
                        cookie.getKey() + COOKIE_NAME_VAL_SEPARATOR + cookie.getValue()
                );
                // .append("Path=/; Domain=");
            }
        }

        try {
            urlConnection.setRequestMethod(requestMethod);
            if ("POST".equals(requestMethod)) {
                //            urlConnection.setReadTimeout(7000);
                //            urlConnection.setConnectTimeout(7000);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                urlConnection.setRequestProperty("Connection", "keep-alive");
                urlConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
                urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                String body = bodyBuilder.toString();
                byte[] bytes = body.getBytes(encoding);
                urlConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
                writeStream(urlConnection.getOutputStream(), bytes);
            }

            urlConnection.connect();

            String responseBody = readStream(urlConnection.getInputStream(), encoding);

            String actualHost = urlConnection.getURL().getHost();
            if (!urlObj.getHost().equals(actualHost)) {
                String message = "Unexpected redirect to: " + actualHost;
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String errorTxt = readStream(urlConnection.getErrorStream(), encoding);
                String message = "Method failed: " + errorTxt;
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }

            List<String> cookiePairs = urlConnection.getHeaderFields().get("Set-Cookie");
            if (cookiePairs == null) {
                return Pair.of(responseBody, new HashMap<>());
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

                return Pair.of(responseBody, cookieMap);
            }

        } catch (Exception e) {
            String errorStream = readStream(urlConnection.getErrorStream(), encoding);
            if (!StringUtil.isTrimmedEmpty(errorStream)) {
                throw new Exception(errorStream);
            }
            String message = "Fatal transport error: " + e.getMessage();
            LOGGER.error(message, e);
            throw e;
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
        try {
            //            urlConnection.setReadTimeout(7000);
            //            urlConnection.setConnectTimeout(7000);
            urlConnection.setRequestMethod("POST");
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
                writeStream(urlConnection.getOutputStream(), bytes);
            }

            urlConnection.connect();
            String responseBody = readStream(urlConnection.getInputStream(), encoding);

            String actualHost = urlConnection.getURL().getHost();
            if (!urlObj.getHost().equals(actualHost)) {
                String message = "Unexpected redirect to: " + actualHost;
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String errorTxt = readStream(urlConnection.getErrorStream(), encoding);
                String message = "Method failed: " + errorTxt;
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }

            List<String> cookiePairs = urlConnection.getHeaderFields().get("Set-Cookie");
            if (cookiePairs == null) {
                return Pair.of(responseBody, new HashMap<String, String>());
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

                return Pair.of(responseBody, cookieMap);
            }
        } catch (Exception e) {
            String message = "Fatal transport error: " + e.getMessage();
            LOGGER.error(message, e);
            String errorMessage = readStream(urlConnection.getErrorStream(), encoding);
            if (!StringUtil.isTrimmedEmpty(errorMessage)) {
                throw new Exception(errorMessage, e);
            } else {
                throw e;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String readStream(InputStream in, String encoding) throws IOException {
        if (in == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for(Object line : IOUtils.readLines(in, encoding)){
            result.append(line);
        }
        return result.toString();
    }

    private static void writeStream(OutputStream outputStream, byte[] body) throws IOException {
        outputStream.write(body);
    }
}
