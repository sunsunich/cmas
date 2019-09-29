package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.hibernate.StaleObjectStateException;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    long createOrUpdateRecord(Diver diver, LogbookEntry formObject, boolean isDraft) throws ParseException, StaleObjectStateException, IOException;

    // image change comes after general logbookEntry properties update
    void imageChanged(LogbookEntry logbookEntry);

    String createSignature(LogbookEntry entry) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
