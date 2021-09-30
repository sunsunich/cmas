package org.cmas.presentation.service.elearning;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.elearning.ElearningToken;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ElearningService {

    @Transactional
    String uploadTokens(MultipartFile file) throws IOException;

    @Nullable
    @Transactional
    ElearningToken getElearningToken(Diver diver) throws Exception;

    void elearningRegister(Diver diver) throws Exception;
}
