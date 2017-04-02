package org.cmas.util.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.cmas.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public final class SimpleHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final int TIME_OUT = 30000;

    private SimpleHttpClient() {
    }

    public static String sendHTTPPostRequestJSON(
            String url,
            String postBody
    ) throws Exception {
        PostMethod method = new PostMethod(url);
        method.setRequestEntity(new StringRequestEntity(postBody, "application/json", Globals.UTF_8_CHARSET));
        return sendHTTPRequest(method);
    }

    public static String sendHTTPPostRequest(
            String url,
            NameValuePair[] params
    ) throws Exception {
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + Globals.UTF_8_CHARSET);
        method.setRequestBody(params);
        return sendHTTPRequest(method);
    }

    public static String sendHTTPGetRequest(
            String url
    ) throws Exception {
        return sendHTTPRequest(new GetMethod(url));
    }

    private static String sendHTTPRequest(HttpMethodBase method) throws IOException {
        try {

            HttpClient client = new HttpClient();
            HttpConnectionManager httpConnectionManager = client.getHttpConnectionManager();
            HttpConnectionManagerParams params = httpConnectionManager.getParams();
            params.setConnectionTimeout(TIME_OUT);
            params.setSoTimeout(TIME_OUT);

            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                String message = "Method failed: " + method.getStatusLine();
                LOG.error(message);
                throw new HttpException(message);
            }

            // Read the response body.
            InputStream inputStream = method.getResponseBodyAsStream();
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, Globals.UTF_8_ENC);
                return writer.toString();
            } finally {
                inputStream.close();
            }

        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }
}
