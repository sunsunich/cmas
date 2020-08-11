package org.cmas.presentation.service;

import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public interface UserFileService {

    @Transactional
    UserFile copyFileFromRegistration(Diver diver, RegFile regFile) throws IOException;

    @Transactional
    UserFile processFile(Diver diver, UserFileType userFileType, MultipartFile multipartFile
//            , boolean fail
    ) throws UserFileException;

    @Transactional
    UserFile processFile(Diver diver, UserFileType userFileType, String base64data
//            , boolean fail
    ) throws UserFileException;

    void processFileRollback(UserFile userFile);
}
