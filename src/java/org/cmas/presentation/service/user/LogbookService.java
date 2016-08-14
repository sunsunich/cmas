package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.model.logbook.LogbookEntryFormObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * Created on Aug 05, 2016
 *
 * @author Alexander Petukhov
 */
public interface LogbookService {

    void createOrUpdateRecord(Diver diver, LogbookEntryFormObject formObject) throws ParseException;

    String createSignature(LogbookEntry entry) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    String createSignature(LogbookEntryFormObject formObject) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException;
}
