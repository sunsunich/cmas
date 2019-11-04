package org.cmas.presentation.service;

import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.diver.Diver;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public interface UserFileService {

    @Transactional
    UserFile processFile(Diver diver, UserFileType userFileType, MultipartFile multipartFile
//            , boolean fail
    ) throws UserFileException;

    void processFileRollback(UserFile userFile);
}
