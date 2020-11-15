package org.cmas.entities.cards;

import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;

/**
 * Created on May 03, 2016
 *
 * @author Alexander Petukhov
 */
public final class CardPrintUtil {

    private CardPrintUtil() {
    }

    @SuppressWarnings("NestedSwitchStatement")
    public static CardPrintInfo toPrintName(PersonalCard card) {
        PersonalCardType cardType = card.getCardType();
        if (cardType == null) {
            cardType = PersonalCardType.NATIONAL;
        }
        DiverLevel diverLevel = card.getDiverLevel();
        if (diverLevel == null) {
            diverLevel = DiverLevel.ONE_STAR;
        }
        DiverType diverType = card.getDiverType();
        if (diverType == null) {
            diverType = DiverType.DIVER;
        }
        CardPrintInfo result = new CardPrintInfo();
        StringBuilder stringBuilder = new StringBuilder();
        switch (cardType) {
            case PRIMARY:
            case NATIONAL:
                stringBuilder.append(diverType);
                result.drawStars = true;
                break;
            case CHILDREN_DIVING:
                switch (diverType) {
                    case DIVER:
                        switch (diverLevel) {
                            case ONE_STAR:
                                stringBuilder.append("BRONZE DOLPHIN");
                                break;
                            case TWO_STAR:
                                stringBuilder.append("SILVER DOLPHIN");
                                break;
                            case THREE_STAR:
                                stringBuilder.append("GOLD DOLPHIN");
                                break;
                            case FOUR_STAR:
                                stringBuilder.append("DIVER P3 CD");// ????
                                break;
                        }
                        break;
                    case INSTRUCTOR:
                        switch (diverLevel) {
                            case ONE_STAR:
                                stringBuilder.append("INSTRUCTOR CD");
                                break;
                            case TWO_STAR:
                                stringBuilder.append("INSTRUCTOR CD");
                                break;
                            case THREE_STAR:
                                stringBuilder.append("INSTRUCTOR TRAINER CD");
                                break;
                            case FOUR_STAR:
                                stringBuilder.append("INSTRUCTOR TRAINER CD");
                                break;
                        }
                        break;
                }
                break;
            case EXTENDED_RANGE:
                stringBuilder.append("EXTENDED RANGE ").append(diverType);
                break;
            case NITROX:
                stringBuilder.append("NITROX ");
                if (diverLevel != DiverLevel.ONE_STAR) {
                    stringBuilder.append("ADVANCED ");
                }
                stringBuilder.append(diverType);
                break;
            case NITROX_GASBLENDER:
                stringBuilder.append("NITROX ").append("GASBLENDER");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");

                }
                break;
            case OXYGEN_ADMINISTATOR:
                stringBuilder.append("OXYGEN ADMINISTRATOR");
                break;
            case TRIMIX:
                switch (diverLevel) {
                    case ONE_STAR:
                        stringBuilder.append("RECREATIONAL ");
                        break;
                    case TWO_STAR:
                        stringBuilder.append("NORMOXIC ");
                        break;
                    case THREE_STAR:
                        stringBuilder.append("ADVANCED ");
                        break;
                    case FOUR_STAR:
                        stringBuilder.append("ADVANCED ");
                        break;
                }
                stringBuilder.append("TRIMIX ").append(diverType);
                break;
            case TRIMIX_GASBLENDER:
                stringBuilder.append("TRIMIX ").append("GASBLENDER");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                break;
            case REBREATHER:
                stringBuilder.append("REBREATHER ");
                switch (diverLevel) {
                    case ONE_STAR:
                        stringBuilder.append("CCR ");
                        break;
                    case TWO_STAR:
                        stringBuilder.append("SCR ");
                        break;
                    case THREE_STAR:
                        stringBuilder.append("SCR ");
                        break;
                    case FOUR_STAR:
                        stringBuilder.append("SCR ");
                        break;
                }
                stringBuilder.append(diverType);
                break;
            case ALTITUDE_DIVER:
                stringBuilder.append("ALTITUDE ").append(diverType);
                break;
            case APNOEA:
                stringBuilder.append("APNOEA");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case CAVE:
                stringBuilder.append("CAVE");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case COMPRESSOR_OPERATOR:
                stringBuilder.append("COMPRESSOR OPERATOR");
                break;
            case DISABLED_DIVING:
                stringBuilder.append("DIS. DIVER");
                switch (diverType) {
                    case DIVER:
                        diverLevelToStringBuilder(diverLevel, stringBuilder);
                        break;
                    case INSTRUCTOR:
                        switch (diverLevel) {
                            case ONE_STAR:
                                stringBuilder.append(" ASSISTANT");
                                break;
                            case TWO_STAR:
                                stringBuilder.append(" INSTRUCTOR");
                                break;
                            case THREE_STAR:
                                stringBuilder.append(" ADV. INSTRUCTOR");
                                break;
                            case FOUR_STAR:
                                stringBuilder.append(" ADV. INSTRUCTOR");
                                break;
                        }
                        break;
                }
                break;
            case DRY_SUIT:
                stringBuilder.append("DRY SUIT ").append(diverType);
                break;
            case DRIFT_DIVING:
                stringBuilder.append("DRIFT DIVING");
                break;
            case GYMSWIMMING:
                stringBuilder.append("GYMSWIMMING ").append(diverType);
                result.drawStars = true;
                break;
            case HYDROBIKE:
                stringBuilder.append("HYDROBIKE ");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append("INSTR. ");
                }
                result.drawStars = true;
                break;
            case ICE_DIVING:
                stringBuilder.append("ICE DIVING ").append(diverType);
                break;
            case INTRO_TO_SCUBA:
                stringBuilder.append("INTRO TO SCUBA");
                break;
            case NAVIGATION:
                stringBuilder.append("NAVIGATION");
                break;
            case NIGHT:
                stringBuilder.append("NIGHT");
                break;
            case PHOTO:
                stringBuilder.append("PHOTO");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case RESCUE:
                if (diverLevel == DiverLevel.ONE_STAR) {
                    stringBuilder.append("RESCUE");
                } else {
                    stringBuilder.append("SELF RESCUE DIVER");
                }
                break;
            case SCOOTER:
                stringBuilder.append("SCOOTER");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case SELF_RESCUE:
                stringBuilder.append("SELF RESCUE");
                break;
            case SIDE_MOUNT:
                stringBuilder.append("SIDE MOUNT ");
                if (diverLevel != DiverLevel.ONE_STAR) {
                    stringBuilder.append("TECHNICAL ");
                }
                stringBuilder.append(diverType);
                break;
            case SKILLS:
                if (diverLevel == DiverLevel.ONE_STAR) {
                    stringBuilder.append("TECHNICAL ");
                } else {
                    stringBuilder.append("ADVANCED ");
                }
                stringBuilder.append("SKILLS ");
                stringBuilder.append(diverType);
                break;
            case SNORKEL:
                stringBuilder.append("SNORKEL");
                if (diverType == DiverType.INSTRUCTOR) {
                    stringBuilder.append(" INSTRUCTOR");
                }
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case WRECK:
                stringBuilder.append("WRECK ").append(diverType);
                diverLevelToStringBuilder(diverLevel, stringBuilder);
                break;
            case SCIENTIFIC:
                switch (diverType) {
                    case DIVER:
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append("ADVANCED ");
                        }
                        stringBuilder.append("SCIENTIFIC DIVER");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("SCIENTIFIC DIVING INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case UNDERWATER_ARCHAEOLOGY:
                switch (diverType) {
                    case DIVER:
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append("ADVANCED ");
                        }
                        stringBuilder.append("UNDERWATER ARCHAEOLOGY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("UNDERWATER ARCHAEOLOGY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case UNDERWATER_GEOLOGY:
                switch (diverType) {
                    case DIVER:
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append("ADVANCED ");
                        }
                        stringBuilder.append("UNDERWATER GEOLOGY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("UNDERWATER GEOLOGY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case FRESHWATER_BIOLOGY:
                switch (diverType) {
                    case DIVER:
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append("ADVANCED ");
                        }
                        stringBuilder.append("FRESHWATER BIOLOGY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("FRESHWATER BIOLOGY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case OCEAN_DISCOVERY:
                switch (diverType) {
                    case DIVER:
                        stringBuilder.append("OCEAN DISCOVERY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("OCEAN DISCOVERY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case MARINE_BIOLOGY:
                switch (diverType) {
                    case DIVER:
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append("ADVANCED ");
                        }
                        stringBuilder.append("MARINE BIOLOGY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("MARINE BIOLOGY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
            case HERITAGE_DISCOVERY:
                switch (diverType) {
                    case DIVER:
                        stringBuilder.append("HERITAGE DISCOVERY COURSE");
                        break;
                    case INSTRUCTOR:
                        stringBuilder.append("HERITAGE DISCOVERY COURSE INSTRUCTOR");
                        if (diverLevel != DiverLevel.ONE_STAR) {
                            stringBuilder.append(" TRAINER");
                        }
                        break;
                }
                break;
        }
        result.printName = stringBuilder.toString();
        return result;
    }

    private static void diverLevelToStringBuilder(DiverLevel diverLevel, StringBuilder stringBuilder) {
        switch (diverLevel) {
            case ONE_STAR:
                stringBuilder.append(" LEVEL I");
                break;
            case TWO_STAR:
                stringBuilder.append(" LEVEL II");
                break;
            case THREE_STAR:
                stringBuilder.append(" LEVEL III");
                break;
            case FOUR_STAR:
                stringBuilder.append(" LEVEL IV");
                break;
        }
    }
//
//    public static Card fromPrintName(String printName) {
//
//    }
}
