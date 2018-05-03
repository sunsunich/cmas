package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * Created on Aug 05, 2016
 *
 * @author Alexander Petukhov
 */
public interface LogbookService {

    long createOrUpdateRecord(Diver diver, LogbookEntry formObject) throws ParseException, IOException;

    String createSignature(LogbookEntry entry) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
