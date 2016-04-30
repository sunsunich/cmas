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
                DiverTypeLevel diverTypeLevel = evalDiverTypeLevel(sheet);
                if (diverTypeLevel == null) {
                    continue;
                }
                for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
                    Row row = sheet.getRow(r);
                    Diver diver = evalDiver(row);
                    if (diver == null) {
                        continue;
                    }
                    diver.setDiverType(diverTypeLevel.diverType);
                    diver.setDiverLevel(diverTypeLevel.diverLevel);
                    PersonalCard card = diver.getSecondaryPersonalCards().get(0);
                    card.setDiverLevel(diverTypeLevel.diverLevel);
                    card.setPersonalCardType(diverTypeLevel.personalCardType);
                    card.setFederationName(diverTypeLevel.federationCardName);

                    Diver existingDiver = divers.get(diver.getEmail());
                    if (existingDiver == null) {
                        divers.put(diver.getEmail(), diver);
                    } else {
                        List<PersonalCard> secondaryPersonalCards = existingDiver.getSecondaryPersonalCards();
                        secondaryPersonalCards.add(card);
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
            instructorCard.setPersonalCardType(PersonalCardType.NATIONAL);
            instructorCard.setNumber(instructorCardNumber);
            List<PersonalCard> secondaryCards = new ArrayList<>(1);
            secondaryCards.add(instructorCard);
            instructor.setSecondaryPersonalCards(secondaryCards);
            diver.setInstructor(instructor);
        }

        PersonalCard card = new PersonalCard();
        card.setNumber(row.getCell(5).getStringCellValue());
        List<PersonalCard> secondaryCards = new ArrayList<>();
        secondaryCards.add(card);
        diver.setSecondaryPersonalCards(secondaryCards);
        return diver;
    }

    @Nullable
    private static DiverTypeLevel evalDiverTypeLevel(Sheet sheet) {
        String sheetName = sheet.getSheetName();
        if (StringUtil.isTrimmedEmpty(sheetName)) {
            return null;
        }
        DiverTypeLevel diverTypeLevel = new DiverTypeLevel();
        char firstChar = sheetName.charAt(0);
        if (firstChar == 'I' || firstChar == 'i' || firstChar == 'И' || firstChar == 'и') {
            diverTypeLevel.diverType = DiverType.INSTRUCTOR;
        } else {
            diverTypeLevel.diverType = DiverType.DIVER;
        }
        char lastChar = sheetName.charAt(sheetName.length() - 1);
        switch (lastChar) {
            case '1':
                diverTypeLevel.diverLevel = DiverLevel.ONE_STAR;
                break;
            case '2':
                diverTypeLevel.diverLevel = DiverLevel.TWO_STAR;
                break;
            case '3':
                diverTypeLevel.diverLevel = DiverLevel.THREE_STAR;
                break;
        }

        Cell sheetHeaderCell = sheet.getRow(0).getCell(0);
        String header = sheetHeaderCell.getRichStringCellValue().getString().toUpperCase(Locale.ENGLISH);
        if (!StringUtil.isTrimmedEmpty(header)) {
            diverTypeLevel.federationCardName = header;
            if (header.contains("NITROX")) {
                diverTypeLevel.personalCardType = PersonalCardType.NITROX;
            } else if (header.contains("ICE")) {
                diverTypeLevel.personalCardType = PersonalCardType.ICE_DIVING;
            } else if (header.contains("DRY")) {
                diverTypeLevel.personalCardType = PersonalCardType.DRY_SUIT;
            } else if (header.contains("SIDE")) {
                diverTypeLevel.personalCardType = PersonalCardType.SIDE_MOUNT;
            } else if (header.contains("CAVE")) {
                diverTypeLevel.personalCardType = PersonalCardType.CAVE;
            } else if (header.contains("TRIMIX")) {
                diverTypeLevel.personalCardType = PersonalCardType.TRIMIX;
            } else if (header.contains("RANGE")) {
                diverTypeLevel.personalCardType = PersonalCardType.EXTENDED_RANGE;
            } else if (header.contains("APNOEA")) {
                diverTypeLevel.personalCardType = PersonalCardType.APNOEA;
            }
        }
        if (diverTypeLevel.personalCardType == null) {
            diverTypeLevel.personalCardType = PersonalCardType.NATIONAL;
        }

        return diverTypeLevel;
    }
}
