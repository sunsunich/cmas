package org.cmas.presentation.controller.cards;

import org.cmas.entities.cards.PersonalCardType;
import org.springframework.beans.factory.InitializingBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created on Apr 20, 2017
 *
 * @author Alexander Petukhov
 */
public class CardDisplayManager implements InitializingBean {

    private final Map<String, PersonalCardType[]> personalCardGroups = new LinkedHashMap<>();

    private final Map<String, PersonalCardType[]> heliCardGroups = new LinkedHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        personalCardGroups.put("cmas.card.type.common",
                               new PersonalCardType[]{PersonalCardType.NATIONAL, PersonalCardType.CHILDREN_DIVING});
        personalCardGroups.put("cmas.card.type.gas.mixing", new PersonalCardType[]{
                PersonalCardType.EXTENDED_RANGE,
                PersonalCardType.NITROX,
                PersonalCardType.NITROX_GASBLENDER,
                PersonalCardType.TRIMIX,
                PersonalCardType.TRIMIX_GASBLENDER,
                PersonalCardType.OXYGEN_ADMINISTATOR,
                PersonalCardType.REBREATHER
        });
        personalCardGroups.put("cmas.card.type.technical", new PersonalCardType[]{
                PersonalCardType.ALTITUDE_DIVER,
                PersonalCardType.APNOEA,
                PersonalCardType.CAVE,
                PersonalCardType.COMPRESSOR_OPERATOR,
                PersonalCardType.DISABLED_DIVING,
                PersonalCardType.DRY_SUIT,
                PersonalCardType.DRIFT_DIVING,
                PersonalCardType.GYMSWIMMING,
                PersonalCardType.HYDROBIKE,
                PersonalCardType.ICE_DIVING,
                PersonalCardType.INTRO_TO_SCUBA,
                PersonalCardType.NAVIGATION,
                PersonalCardType.NIGHT,
                PersonalCardType.PHOTO,
                PersonalCardType.RESCUE,
                PersonalCardType.SCOOTER,
                PersonalCardType.SELF_RESCUE,
                PersonalCardType.SIDE_MOUNT,
                PersonalCardType.SKILLS,
                PersonalCardType.SNORKEL,
                PersonalCardType.WRECK,
                });
        personalCardGroups.put("cmas.card.type.scientific", new PersonalCardType[]{
                PersonalCardType.SCIENTIFIC,
                PersonalCardType.UNDERWATER_ARCHAEOLOGY,
                PersonalCardType.UNDERWATER_GEOLOGY,
                PersonalCardType.FRESHWATER_BIOLOGY,
                PersonalCardType.OCEAN_DISCOVERY,
                PersonalCardType.MARINE_BIOLOGY,
                PersonalCardType.HERITAGE_DISCOVERY
        });
        heliCardGroups.put("cmas.card.type.heli", new PersonalCardType[]{
                PersonalCardType.HELI_DIVER,
                PersonalCardType.HELI_RESCUE,
                PersonalCardType.POWERBOAT_RESCUE
        });
    }

    public Map<String, PersonalCardType[]> getPersonalCardGroups() {
        return personalCardGroups;
    }

    public Map<String, PersonalCardType[]> getHeliCardGroups() {
        return heliCardGroups;
    }
}
