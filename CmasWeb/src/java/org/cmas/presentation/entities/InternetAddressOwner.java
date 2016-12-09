package org.cmas.presentation.entities;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

public interface InternetAddressOwner {
    InternetAddress getInternetAddress() throws UnsupportedEncodingException;
    Long getNullableId();
}