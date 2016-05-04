package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("MagicCharacter")
public class RusDiverXlsParserImpl implements DiverXlsParser {

    @Override
    public Collection<Diver> getDivers(InputStream file) throws Exception {
        try (Workbook wb = WorkbookFactory.create(file)) {
            Map<String, Diver> divers = new HashMap<>();
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                PersonalCard globalCard = evalDiverTypeLevel(sheet);
                if (globalCard == null) {
                    continue;
                }
                for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
                    Row row = sheet.getRow(r);
                    Diver diver = evalDiver(row);
                    if (diver == null) {
                        continue;
                    }
                    diver.setDiverType(globalCard.getDiverType());
                    diver.setDiverLevel(globalCard.getDiverLevel());
                    PersonalCard card = diver.getCards().get(0);
                    card.setDiverType(globalCard.getDiverType());
                    card.setDiverLevel(globalCard.getDiverLevel());
                    card.setCardType(globalCard.getCardType());
                    card.setFederationName(globalCard.getFederationName());

                    Diver existingDiver = divers.get(diver.getEmail());
                    if (existingDiver == null) {
                        divers.put(diver.getEmail(), diver);
                    } else {
                        List<PersonalCard> existingDiverCards = existingDiver.getCards();
                        existingDiverCards.add(card);
                        if (diver.getDiverType() == DiverType.INSTRUCTOR) {
                            existingDiver.setDiverType(DiverType.INSTRUCTOR);
                        }
                        DiverLevel newDiverLevel = diver.getDiverLevel();
                        if (newDiverLevel != null) {
                            DiverLevel existingDiverLevel = existingDiver.getDiverLevel();
                            if (existingDiverLevel == null
                                || existingDiverLevel.ordinal() < newDiverLevel.ordinal()) {
                                existingDiver.setDiverLevel(newDiverLevel);
                            }
                        }
                    }
                }
            }
            return divers.values();
        }
    }

    @Override
    public Collection<Diver> getDivers(File file) throws Exception {
        return getDivers(new FileInputStream(file));
    }

    @Nullable
    private static Diver evalDiver(Row row) {
        if (row == null || row.getPhysicalNumberOfCells() < 10) {
            return null;
        }
        Diver diver = new Diver();
        String[] firstNameAndLastName = row.getCell(0).getStringCellValue().split(" ");
        if (firstNameAndLastName.length < 2) {
            return null;
        }
        String email = row.getCell(9).getStringCellValue();
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        diver.setFirstName(firstNameAndLastName[1]);
        diver.setLastName(firstNameAndLastName[0]);
        diver.setDob(row.getCell(4).getDateCellValue());
        diver.setEmail(email);

        String instructorCardNumber = row.getCell(7).getStringCellValue();
        if (!StringUtil.isTrimmedEmpty(instructorCardNumber)) {
            Diver instructor = new Diver();
            instructor.setDiverType(DiverType.INSTRUCTOR);
            PersonalCard instructorCard = new PersonalCard();
            instructorCard.setCardType(PersonalCardType.NATIONAL);
            instructorCard.setDiverType(DiverType.INSTRUCTOR);
            instructorCard.setNumber(instructorCardNumber);
            List<PersonalCard> cards = new ArrayList<>(1);
            cards.add(instructorCard);
            instructor.setCards(cards);
            diver.setInstructor(instructor);
        }

        PersonalCard card = new PersonalCard();
        card.setNumber(row.getCell(5).getStringCellValue());
        List<PersonalCard> cards = new ArrayList<>();
        cards.add(card);
        diver.setCards(cards);
        return diver;
    }

    @Nullable
    private static PersonalCard evalDiverTypeLevel(Sheet sheet) {
        String sheetName = sheet.getSheetName();
        if (StringUtil.isTrimmedEmpty(sheetName)) {
            return null;
        }
        PersonalCard card = new PersonalCard();
        char firstChar = sheetName.charAt(0);
        if (firstChar == 'I' || firstChar == 'i' || firstChar == 'И' || firstChar == 'и') {
            card.setDiverType(DiverType.INSTRUCTOR);
        } else {
            card.setDiverType(DiverType.DIVER);
        }
        char lastChar = sheetName.charAt(sheetName.length() - 1);
        switch (lastChar) {
            case '1':
                card.setDiverLevel(DiverLevel.ONE_STAR);
                break;
            case '2':
                card.setDiverLevel(DiverLevel.TWO_STAR);
                break;
            case '3':
                card.setDiverLevel(DiverLevel.THREE_STAR);
                break;
        }
        Cell sheetHeaderCell = sheet.getRow(0).getCell(0);
        String header = sheetHeaderCell.getRichStringCellValue().getString().toUpperCase(Locale.ENGLISH);
        if (!StringUtil.isTrimmedEmpty(header)) {
            card.setFederationName(header);
            if (header.contains("CHILDREN")) {
                card.setCardType(PersonalCardType.CHILDREN_DIVING);
            } else if (header.contains("ALTITUDE")) {
                card.setCardType(PersonalCardType.ALTITUDE_DIVER);
            } else if (header.contains("COMPRESSOR")) {
                card.setCardType(PersonalCardType.COMPRESSOR_OPERATOR);
            } else if (header.contains("DISABLED")) {
                card.setCardType(PersonalCardType.DISABLED_DIVING);
            } else if (header.contains("DRIFT")) {
                card.setCardType(PersonalCardType.DRIFT_DIVING);
            } else if (header.contains("GYMSWIMMING")) {
                card.setCardType(PersonalCardType.GYMSWIMMING);
            } else if (header.contains("HYDROBIKE")) {
                card.setCardType(PersonalCardType.HYDROBIKE);
            } else if (header.contains("INTRO")) {
                card.setCardType(PersonalCardType.INTRO_TO_SCUBA);
            } else if (header.contains("NAVIGATION")) {
                card.setCardType(PersonalCardType.NAVIGATION);
            } else if (header.contains("NIGHT")) {
                card.setCardType(PersonalCardType.NIGHT);
            } else if (header.contains("PHOTO")) {
                card.setCardType(PersonalCardType.PHOTO);
            } else if (header.contains("RESCUE") && header.contains("SELF")) {
                card.setCardType(PersonalCardType.SELF_RESCUE);
            } else if (header.contains("RESCUE")) {
                card.setCardType(PersonalCardType.RESCUE);
            } else if (header.contains("SCOOTER")) {
                card.setCardType(PersonalCardType.SCOOTER);
            } else if (header.contains("SKILLS")) {
                card.setCardType(PersonalCardType.SKILLS);
            } else if (header.contains("SNORKEL")) {
                card.setCardType(PersonalCardType.SNORKEL);
            } else if (header.contains("WRECK")) {
                card.setCardType(PersonalCardType.WRECK);
            } else if (header.contains("SCIENTIFIC")) {
                card.setCardType(PersonalCardType.SCIENTIFIC);
            } else if (header.contains("ARCHAEOLOGY")) {
                card.setCardType(PersonalCardType.UNDERWATER_ARCHAEOLOGY);
            } else if (header.contains("GEOLOGY")) {
                card.setCardType(PersonalCardType.UNDERWATER_GEOLOGY);
            } else if (header.contains("BIOLOGY") && header.contains("FRESHWATER")) {
                card.setCardType(PersonalCardType.FRESHWATER_BIOLOGY);
            } else if (header.contains("BIOLOGY") && header.contains("MARINE")) {
                card.setCardType(PersonalCardType.MARINE_BIOLOGY);
            } else if (header.contains("OCEAN") && header.contains("DISCOVERY")) {
                card.setCardType(PersonalCardType.OCEAN_DISCOVERY);
            } else if (header.contains("HERITAGE") && header.contains("DISCOVERY")) {
                card.setCardType(PersonalCardType.HERITAGE_DISCOVERY);
            } else if (header.contains("REBREATHER")) {
                card.setCardType(PersonalCardType.REBREATHER);
            } else if (header.contains("OXYGEN")) {
                card.setCardType(PersonalCardType.OXYGEN_ADMINISTATOR);
            } else if (header.contains("TRIMIX") && header.contains("GASBLENDER")) {
                card.setCardType(PersonalCardType.TRIMIX_GASBLENDER);
            } else if (header.contains("TRIMIX")) {
                card.setCardType(PersonalCardType.TRIMIX);
            } else if (header.contains("NITROX") && header.contains("GASBLENDER")) {
                card.setCardType(PersonalCardType.NITROX_GASBLENDER);
            } else if (header.contains("NITROX")) {
                card.setCardType(PersonalCardType.NITROX);
            } else if (header.contains("ICE")) {
                card.setCardType(PersonalCardType.ICE_DIVING);
            } else if (header.contains("DRY")) {
                card.setCardType(PersonalCardType.DRY_SUIT);
            } else if (header.contains("SIDE")) {
                card.setCardType(PersonalCardType.SIDE_MOUNT);
            } else if (header.contains("CAVE")) {
                card.setCardType(PersonalCardType.CAVE);
            } else if (header.contains("RANGE")) {
                card.setCardType(PersonalCardType.EXTENDED_RANGE);
            } else if (header.contains("APNOEA")) {
                card.setCardType(PersonalCardType.APNOEA);
            }
        }
        if (card.getCardType() == null) {
            card.setCardType(PersonalCardType.NATIONAL);
        }
        return card;
    }
}
