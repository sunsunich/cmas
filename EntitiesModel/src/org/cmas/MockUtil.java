package org.cmas;

import org.cmas.entities.Country;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on Dec 07, 2015
 *
 * @author Alexander Petukhov
 */
public final class MockUtil {

    private static final Map<String, Diver> users = new HashMap<>();

    static {
        Diver instructor = new Diver(1L);
        instructor.setEmail("a1@gmail.com");
        instructor.setPassword("aaa");
        instructor.setCountry(getCountry());
        instructor.setDob(new Date());
        instructor.setFirstName("Christopher");
        instructor.setLastName("Wolfeschlegelsteinhausenbergerdorff");

        instructor.setDiverLevel(DiverLevel.THREE_STAR);
        instructor.setDiverType(DiverType.INSTRUCTOR);

        NationalFederation federation = getSportsFederation();
        PersonalCard card = new PersonalCard();
        card.setNumber("5510123456789001");
        card.setDiverLevel(instructor.getDiverLevel());
        card.setDiver(instructor);
        instructor.setFederation(federation);
        instructor.setPrimaryPersonalCard(card);

        users.put("a1@gmail.com", instructor);
    }

    private MockUtil() {
    }

    public static Diver getDiver(){
        return users.values().iterator().next();
    }

//    public static Pair<User, String> loginMockDiver(String email, String password) {
//        User diver = users.get(email);
//        if (diver == null) {
//            return new Pair<>(null, ErrorCodes.NO_SUCH_USER);
//        }
//        if (diver.getPassword().equals(password)) {
//            return new Pair<>(diver, "");
//        } else {
//            return new Pair<>(null, ErrorCodes.WRONG_PASSWORD);
//        }
//    }

    public static List<Diver> searchUsers(String query, DiverType diverType) {
        String lowerCaseQuery = query.toLowerCase();
        List<Diver> result = new ArrayList<>();
        for (Diver diver : users.values()) {
            if (   diver.getDiverType() == diverType
                && (   diver.getFirstName().toLowerCase().startsWith(lowerCaseQuery)
                    || diver.getLastName().toLowerCase().startsWith(lowerCaseQuery))
            ) {
                result.add(diver);
            }
        }
        return result;
    }

    private static Country getCountry() {
        return new Country("us");
    }

    private static NationalFederation getSportsFederation() {
        NationalFederation federation = new NationalFederation();
        federation.setName("THE UNDERWATER SOCIETY OF AMERICA");
        return federation;
    }

}
