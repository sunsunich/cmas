package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.util.Base64Coder;
import org.cmas.util.random.Randomazer;
import org.springframework.beans.factory.annotation.Autowired;

public class DiverMobileServiceImpl implements DiverMobileService {

    @Autowired
    private Randomazer randomazer;

    @Override
    public void setMobileAuthCode(Diver diver) {
        diver.setMobileAuthToken(
                Base64Coder.encodeString(randomazer.generateRandomStringByUniqueId(
                        diver.getId(), Globals.USER_TOKEN_RAND_PART_LENGTH)
                )
        );
    }
}
