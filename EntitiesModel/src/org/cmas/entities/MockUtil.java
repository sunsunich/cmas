package org.cmas.entities;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.SportsFederation;

import java.util.Date;

/**
 * Created on Dec 07, 2015
 *
 * @author Alexander Petukhov
 */
public class MockUtil {

    public static Diver getMockDiver(){
        Diver user = new Diver(1L);
        user.setEmail("a1@gmail.cpm");
        user.setPassword("aaaa");
        user.setCountry(new Country("us"));
        user.setDob(new Date());
        user.setFirstName("John");
        user.setLastName("Smith");
        SportsFederation federation = new SportsFederation();
        federation.setName("THE UNDERWATER SOCIETY OF AMERICA");
        PersonalCard card = new PersonalCard();
        card.setNumber("5510123456789001");
        user.setFederation(federation);
        user.setPrimaryPersonalCard(card);

        return user;
    }

}
