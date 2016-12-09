package org.cmas.util.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class SimpleHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final int TIME_OUT = 30000;

    private SimpleHttpClient() {
    }

    public static String sendHTTPPostRequest(
            String url,
            String postBody,
            String encoding
    ) throws Exception {
        PostMethod method = new PostMethod(url);
        method.setRequestEntity(new StringRequestEntity(postBody,"application/json", null));        
        return sendHTTPRequest(encoding, method);
    }

    public static String sendHTTPGetRequest(
            String url,
            String encoding
    ) throws Exception {

        GetMethod method = new GetMethod(url);
        //method.setURI(new URI(url, false));

        return sendHTTPRequest(encoding, method);
    }

    private static String sendHTTPRequest(String encoding, HttpMethodBase method) throws IOException {
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
                IOUtils.copy(inputStream, writer, encoding);
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
