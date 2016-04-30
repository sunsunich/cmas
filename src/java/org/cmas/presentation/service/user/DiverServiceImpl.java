package org.cmas.presentation.service.user;

import org.cmas.backend.xls.DiverXlsParser;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.Role;
import org.cmas.entities.UserBalance;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.util.text.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverServiceImpl extends UserServiceImpl<Diver> implements DiverService {

    @Autowired
    @Qualifier("rusDiverXlsParser")
    private DiverXlsParser rusDiverXlsParser;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardDao personalCardDao;

    @SuppressWarnings("CallToStringEqualsIgnoreCase")
    @Override
    public void uploadDivers(NationalFederation federation, InputStream file) throws Exception {
        Collection<Diver> divers = null;
        Locale locale = null;
        if ("RUS".equalsIgnoreCase(federation.getCountry().getCode())) {
            divers = rusDiverXlsParser.getDivers(file);
            locale = new Locale("ru");
        }
        if (divers == null) {
            throw new UnsupportedOperationException("Federation not supported");
        }
        for (Diver diver : divers) {
            Diver dbInstructor = null;
            Diver instructor = diver.getInstructor();
            if (instructor != null) {
                dbInstructor = diverDao.getDiverBySecondaryCardNumber(
                        instructor.getSecondaryPersonalCards().get(0).getNumber()
                );
            }
            if (dbInstructor == null) {
                diver.setInstructor(null);
            } else {
                diver.setInstructor(dbInstructor);
            }
            String email = diver.getEmail();
            Diver dbDiver = diverDao.getByEmail(email);
            boolean isNew = dbDiver == null;
            if (isNew) {
                dbDiver = diverDao.createNew(Diver.class);
                dbDiver.setLocale(locale);
                dbDiver.setFederation(federation);
                dbDiver.setCountry(federation.getCountry());
                dbDiver.setRole(Role.ROLE_DIVER);
                dbDiver.setEmail(email);

                UserBalance userBalance = new UserBalance();
                userBalanceDao.save(userBalance);
                dbDiver.setUserBalance(userBalance);
            }
            String firstName = diver.getFirstName();
            if (!StringUtil.isTrimmedEmpty(firstName)) {
                dbDiver.setFirstName(firstName);
            }
            String lastName = diver.getLastName();
            if (!StringUtil.isTrimmedEmpty(lastName)) {
                dbDiver.setLastName(lastName);
            }
            Date dob = diver.getDob();
            if (dob != null) {
                dbDiver.setDob(dob);
            }
            DiverType diverType = diver.getDiverType();
            if (diverType != null) {
                dbDiver.setDiverType(diverType);
            }
            DiverLevel diverLevel = diver.getDiverLevel();
            if (diverLevel != null) {
                dbDiver.setDiverLevel(diverLevel);
            }
            if (dbDiver.getInstructor() == null && diver.getInstructor() != null) {
                dbDiver.setInstructor(diver.getInstructor());
            }

            if (isNew) {
                diverDao.save(dbDiver);
            } else {
                diverDao.updateModel(dbDiver);
            }
            for (PersonalCard card : diver.getSecondaryPersonalCards()) {
                PersonalCard dbCard = personalCardDao.getByNumber(card.getNumber());
                boolean isNewCard = dbCard == null;
                if (isNewCard) {
                    dbCard = card;
                } else {
                    dbCard.setFederationName(card.getFederationName());
                    dbCard.setPersonalCardType(card.getPersonalCardType());
                    dbCard.setDiverLevel(card.getDiverLevel());
                }
                dbCard.setDiver(dbDiver);
                if (isNewCard) {
                    personalCardDao.save(dbCard);
                } else {
                    personalCardDao.updateModel(dbCard);
                }
            }
        }
    }
}
