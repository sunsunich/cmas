package org.cmas.presentation.controller.mobile.dictionary;

import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created on Oct 10, 2016
 *
 * @author Alexander Petukhov
 */
public class FCMSender extends Sender {

    public FCMSender(String key) {
        super(key);
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    protected HttpURLConnection getConnection(String url) throws IOException {
        String fcmUrl = "https://fcm.googleapis.com/fcm/send";
        return (HttpURLConnection) new URL(fcmUrl).openConnection();
    }
}
