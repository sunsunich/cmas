package org.cmas.backend.xls;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Locale;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("MagicCharacter")
public abstract class BaseDiverXlsParserImpl implements DiverXlsParser {

    @Override
    public Collection<Diver> getDivers(File file) throws Exception {
        return getDivers(new FileInputStream(file));
    }

    protected static void setDiverLevelFromInt(PersonalCard card, int levelChar) {
        switch (levelChar) {
            case 1:
                card.setDiverLevel(DiverLevel.ONE_STAR);
                break;
            case 2:
                card.setDiverLevel(DiverLevel.TWO_STAR);
                break;
            case 3:
                card.setDiverLevel(DiverLevel.THREE_STAR);
                break;
            case 4:
                card.setDiverLevel(DiverLevel.FOUR_STAR);
                break;
        }
    }

    protected static void setCardTypeFromStr(PersonalCard card, String str) {
        String fixedInputStr = str;
        if (fixedInputStr == null) {
            fixedInputStr = "";
        }
        fixedInputStr = fixedInputStr.toUpperCase(Locale.ENGLISH);
        if (fixedInputStr.contains("CHILDREN")) {
            card.setCardType(PersonalCardType.CHILDREN_DIVING);
        } else if (fixedInputStr.contains("ALTITUDE")) {
            card.setCardType(PersonalCardType.ALTITUDE_DIVER);
        } else if (fixedInputStr.contains("COMPRESSOR")) {
            card.setCardType(PersonalCardType.COMPRESSOR_OPERATOR);
        } else if (fixedInputStr.contains("DISABLED")) {
            card.setCardType(PersonalCardType.DISABLED_DIVING);
        } else if (fixedInputStr.contains("DRIFT")) {
            card.setCardType(PersonalCardType.DRIFT_DIVING);
        } else if (fixedInputStr.contains("GYMSWIMMING")) {
            card.setCardType(PersonalCardType.GYMSWIMMING);
        } else if (fixedInputStr.contains("HYDROBIKE")) {
            card.setCardType(PersonalCardType.HYDROBIKE);
        } else if (fixedInputStr.contains("INTRO")) {
            card.setCardType(PersonalCardType.INTRO_TO_SCUBA);
        } else if (fixedInputStr.contains("NAVIGATION")) {
            card.setCardType(PersonalCardType.NAVIGATION);
        } else if (fixedInputStr.contains("NIGHT")) {
            card.setCardType(PersonalCardType.NIGHT);
        } else if (fixedInputStr.contains("PHOTO")) {
            card.setCardType(PersonalCardType.PHOTO);
        } else if (fixedInputStr.contains("RESCUE") && fixedInputStr.contains("SELF")) {
            card.setCardType(PersonalCardType.SELF_RESCUE);
        } else if (fixedInputStr.contains("RESCUE")) {
            card.setCardType(PersonalCardType.RESCUE);
        } else if (fixedInputStr.contains("SCOOTER")) {
            card.setCardType(PersonalCardType.SCOOTER);
        } else if (fixedInputStr.contains("SKILLS")) {
            card.setCardType(PersonalCardType.SKILLS);
        } else if (fixedInputStr.contains("SNORKEL")) {
            card.setCardType(PersonalCardType.SNORKEL);
        } else if (fixedInputStr.contains("WRECK")) {
            card.setCardType(PersonalCardType.WRECK);
        } else if (fixedInputStr.contains("SCIENTIFIC")) {
            card.setCardType(PersonalCardType.SCIENTIFIC);
        } else if (fixedInputStr.contains("ARCHAEOLOGY")) {
            card.setCardType(PersonalCardType.UNDERWATER_ARCHAEOLOGY);
        } else if (fixedInputStr.contains("GEOLOGY")) {
            card.setCardType(PersonalCardType.UNDERWATER_GEOLOGY);
        } else if (fixedInputStr.contains("BIOLOGY") && fixedInputStr.contains("FRESHWATER")) {
            card.setCardType(PersonalCardType.FRESHWATER_BIOLOGY);
        } else if (fixedInputStr.contains("BIOLOGY") && fixedInputStr.contains("MARINE")) {
            card.setCardType(PersonalCardType.MARINE_BIOLOGY);
        } else if (fixedInputStr.contains("OCEAN") && fixedInputStr.contains("DISCOVERY")) {
            card.setCardType(PersonalCardType.OCEAN_DISCOVERY);
        } else if (fixedInputStr.contains("HERITAGE") && fixedInputStr.contains("DISCOVERY")) {
            card.setCardType(PersonalCardType.HERITAGE_DISCOVERY);
        } else if (fixedInputStr.contains("REBREATHER")) {
            card.setCardType(PersonalCardType.REBREATHER);
        } else if (fixedInputStr.contains("OXYGEN")) {
            card.setCardType(PersonalCardType.OXYGEN_ADMINISTATOR);
        } else if (fixedInputStr.contains("TRIMIX") && fixedInputStr.contains("GASBLENDER")) {
            card.setCardType(PersonalCardType.TRIMIX_GASBLENDER);
        } else if (fixedInputStr.contains("TRIMIX")) {
            card.setCardType(PersonalCardType.TRIMIX);
        } else if (fixedInputStr.contains("NITROX") && fixedInputStr.contains("GASBLENDER")) {
            card.setCardType(PersonalCardType.NITROX_GASBLENDER);
        } else if (fixedInputStr.contains("NITROX")) {
            card.setCardType(PersonalCardType.NITROX);
        } else if (fixedInputStr.contains("ICE")) {
            card.setCardType(PersonalCardType.ICE_DIVING);
        } else if (fixedInputStr.contains("DRY")) {
            card.setCardType(PersonalCardType.DRY_SUIT);
        } else if (fixedInputStr.contains("SIDE")) {
            card.setCardType(PersonalCardType.SIDE_MOUNT);
        } else if (fixedInputStr.contains("CAVE")) {
            card.setCardType(PersonalCardType.CAVE);
        } else if (fixedInputStr.contains("RANGE")) {
            card.setCardType(PersonalCardType.EXTENDED_RANGE);
        } else if (fixedInputStr.contains("APNOEA")) {
            card.setCardType(PersonalCardType.APNOEA);
        }

        if (card.getCardType() == null) {
            card.setCardType(PersonalCardType.NATIONAL);
        }
    }
}