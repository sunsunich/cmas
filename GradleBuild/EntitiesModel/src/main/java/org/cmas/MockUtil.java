package org.cmas;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.LongSerializationPolicy;
import org.cmas.entities.Country;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
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
        instructor.setDiverRegistrationStatus(DiverRegistrationStatus.GUEST);

        instructor.setDiverLevel(DiverLevel.THREE_STAR);
        instructor.setDiverType(DiverType.INSTRUCTOR);

        NationalFederation federation = getSportsFederation();
        PersonalCard primaryCard = new PersonalCard();
        primaryCard.setCardType(PersonalCardType.PRIMARY);
        primaryCard.setNumber("123");
        primaryCard.setDiverLevel(instructor.getDiverLevel());
        primaryCard.setDiver(instructor);
        instructor.setFederation(federation);
        instructor.setPrimaryPersonalCard(primaryCard);

        users.put("a1@gmail.com", instructor);
    }

    private MockUtil() {
    }

    public static Diver getDiver() {
        return users.values().iterator().next();
    }

    public static Diver getHeliDiver() {
        Diver heliDiver = new Diver(1L);
        heliDiver.setEmail("heli1@mailinator.com");
        heliDiver.setPassword("aaa");
        heliDiver.setCountry(getCountry());
        heliDiver.setDob(new Date());
        heliDiver.setFirstName("Christopher");
        heliDiver.setLastName("Wolfeschlegelsteinhausenbergerdorff");

//        heliDiver.setFirstName("Alexander");
//        heliDiver.setLastName("Petukhov");
        heliDiver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
        heliDiver.setDiverLevel(DiverLevel.THREE_STAR);
        heliDiver.setDiverType(DiverType.INSTRUCTOR);

        List<PersonalCard> cards = new ArrayList<>();
        heliDiver.setCards(cards);
//        {
//            PersonalCard card = new PersonalCard();
//            card.setCardType(PersonalCardType.UNDERWATER_ARCHAEOLOGY);
//            card.setNumber("HD/123123123/123123/123");
//            card.setDiverLevel(DiverLevel.THREE_STAR);
//            card.setDiverType(DiverType.INSTRUCTOR);
//            card.setDiver(heliDiver);
//            cards.add(card);
//        }
//        {
//            PersonalCard card = new PersonalCard();
//            card.setCardType(PersonalCardType.HELI_DIVER);
//            card.setNumber("HD/123123123/123123/123");
//            card.setDiverLevel(DiverLevel.ONE_STAR);
//            card.setDiverType(DiverType.DIVER);
//            card.setDiver(heliDiver);
//            cards.add(card);
//        }
//        {
//            PersonalCard card = new PersonalCard();
//            card.setCardType(PersonalCardType.HELI_RESCUE);
//            card.setNumber("HD/123123123/123123/123");
//            card.setDiverLevel(DiverLevel.ONE_STAR);
//            card.setDiverType(DiverType.DIVER);
//            card.setDiver(heliDiver);
//            cards.add(card);
//        }
        {
            PersonalCard card = new PersonalCard();
            card.setCardType(PersonalCardType.POWERBOAT_RESCUE);
            card.setNumber("HD/123123123/123123/123");
            card.setDiverLevel(DiverLevel.ONE_STAR);
            card.setDiverType(DiverType.DIVER);
            card.setDiver(heliDiver);
            cards.add(card);
        }
//        {
//            PersonalCard card = new PersonalCard();
//            card.setCardType(PersonalCardType.APNOEA);
//            card.setNumber("HD/123123123/123123/123");
//            card.setDiverLevel(DiverLevel.TWO_STAR);
//            card.setDiverType(DiverType.INSTRUCTOR);
//            card.setDiver(heliDiver);
//            cards.add(card);
//        }
        {
            PersonalCard card = new PersonalCard();
            card.setCardType(PersonalCardType.NATIONAL);
            card.setNumber("HD/123123123/123123/123");
            card.setDiverLevel(DiverLevel.TWO_STAR);
            card.setDiverType(DiverType.INSTRUCTOR);
            card.setDiver(heliDiver);
            cards.add(card);
        }
//        {
//            PersonalCard card = new PersonalCard();
//            card.setCardType(PersonalCardType.NITROX_GASBLENDER);
//            card.setNumber("HD/123123123/123123/123");
//            card.setDiverLevel(DiverLevel.ONE_STAR);
//            card.setDiverType(DiverType.INSTRUCTOR);
//            card.setDiver(heliDiver);
//            cards.add(card);
//        }

        PersonalCard primaryCard = new PersonalCard();
        primaryCard.setCardType(PersonalCardType.PRIMARY);
        primaryCard.setNumber("1233456712343456");
        primaryCard.setDiverLevel(heliDiver.getDiverLevel());
        primaryCard.setDiverType(heliDiver.getDiverType());
        primaryCard.setDiver(heliDiver);
        cards.add(primaryCard);
        heliDiver.setPrimaryPersonalCard(primaryCard);

        return heliDiver;
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
            if (diver.getDiverType() == diverType
                && (diver.getFirstName().toLowerCase().startsWith(lowerCaseQuery)
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

    public static void main(String[] args) {
        Diver diver = new Diver();
        diver.setId(7044166216760470125L);
        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .create();
        String json = gson.toJson(diver);
        System.out.println(json);
        Gson gson2 = new GsonBuilder()
                // .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .create();
        Diver fromJson = gson2.fromJson(json, Diver.class);
        System.out.println(fromJson.getId());
    }

}
