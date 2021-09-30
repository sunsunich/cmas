package org.cmas.presentation.service.elearning;

import org.apache.commons.io.IOUtils;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.elearning.ElearningToken;
import org.cmas.entities.elearning.ElearningTokenStatus;
import org.cmas.presentation.dao.elearning.ElearningTokenDao;
import org.cmas.presentation.service.mail.MailService;
import org.hibernate.StaleObjectStateException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElearningServiceImpl implements ElearningService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElearningServiceImpl.class);

    private static final int TOKEN_CNT_WARNING_THRESHOLD = 100;

    @Autowired
    private ElearningTokenDao elearningTokenDao;

    @Autowired
    private MailService mailService;

    private final Lock tokenLock = new ReentrantLock();

    @Transactional
    @Override
    public String uploadTokens(MultipartFile file) throws IOException {
        if (file == null) {
            return "validation.emptyField";
        }
        InputStream inputStream = file.getInputStream();
        @SuppressWarnings("unchecked")
        List<String> list = IOUtils.readLines(inputStream);
        for (String token : list) {
            ElearningToken elearningToken = new ElearningToken();
            elearningToken.setToken(token);
            elearningTokenDao.save(elearningToken);
        }
        return "";
    }

    @Override
    public void elearningRegister(Diver diver) throws Exception {
        ElearningToken token = elearningTokenDao.getByDiver(diver);
        if (token == null) {
            throw new IllegalStateException("cannot register non existent token for diver id = " + diver.getId());
        }
        token.setStatus(ElearningTokenStatus.CLICKED);
        elearningTokenDao.updateModel(token);
    }

    @Nullable
    @Transactional
    @Override
    public ElearningToken getElearningToken(Diver diver) throws Exception {
        ElearningToken token = elearningTokenDao.getByDiver(diver);
        if (token != null) {
            return token;
        }
        token = setupElearningToken(diver);
        if (elearningTokenDao.countAvailableTokens() < TOKEN_CNT_WARNING_THRESHOLD) {
            mailService.sendElearningTokensWarning();
        }
        return token;
    }

    private static final int MAX_TOKEN_SET_ATTEMPT = 5;

    private ElearningToken setupElearningToken(Diver diver) throws Exception {
        tokenLock.lock();
        try {
            int tokenSetAttemptCnt = 0;
            while (tokenSetAttemptCnt < MAX_TOKEN_SET_ATTEMPT) {
                ElearningToken newToken = elearningTokenDao.getNextNewToken();
                if (newToken == null) {
                    // this failure is related to the fact that we run out of tokens
                    return null;
                }
                try {
                    newToken.setDiver(diver);
                    newToken.setStatus(ElearningTokenStatus.ASSIGNED);
                    elearningTokenDao.updateModel(newToken);
                    return newToken;
                } catch (StaleObjectStateException e) {
                    tokenSetAttemptCnt++;
                    LOGGER.error("Failed to set e-learning token for diver id = " + diver.getId(), e);
                }
            }
            // this failure is related to the temporary DB error
            throw new Exception("Failed to set e-learning token for diver after "
                                + MAX_TOKEN_SET_ATTEMPT
                                + " attempts");
        } finally {
            tokenLock.unlock();
        }
    }
}
