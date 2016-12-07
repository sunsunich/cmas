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
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;

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
public class RusDiverXlsParserImpl extends BaseDiverXlsParserImpl {

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

    @Nullable
    private static Diver evalDiver(Row row) {
        if (row == null || row.getPhysicalNumberOfCells() < 10) {
            return null;
        }
        Diver diver = new Diver();
        String[] firstNameAndLastName = StringUtil.correctSpaceCharAndTrim(row.getCell(0).getStringCellValue())
                                                  .split(" ");
        if (firstNameAndLastName.length < 2) {
            return null;
        }
        String email = StringUtil.correctSpaceCharAndTrim(row.getCell(9).getStringCellValue());
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        diver.setFirstName(firstNameAndLastName[1]);
        diver.setLastName(firstNameAndLastName[0]);
        diver.setDob(row.getCell(4).getDateCellValue());
        diver.setEmail(email);

        String instructorCardNumber = StringUtil.correctSpaceCharAndTrim(row.getCell(7).getStringCellValue());
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
        card.setNumber(StringUtil.correctSpaceCharAndTrim(row.getCell(5).getStringCellValue()));
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
        try {
            int lastChar = Integer.parseInt(sheetName.substring(sheetName.length() - 1));
            setDiverLevelFromInt(card, lastChar);
        } catch (Exception ignored) {
        }

        Cell sheetHeaderCell = sheet.getRow(0).getCell(0);
        String header = StringUtil.correctSpaceCharAndTrim(sheetHeaderCell.getStringCellValue());
        if (!StringUtil.isTrimmedEmpty(header)) {
            card.setFederationName(header.toUpperCase(Locale.ENGLISH));
        }
        setCardTypeFromStr(card, header);

        return card;
    }
}
