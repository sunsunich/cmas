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
import java.util.Collections;
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
public class SingleTableDiverXlsParserImpl extends BaseDiverXlsParserImpl {

    @Override
    public Collection<Diver> getDivers(InputStream file) throws Exception {
        try (Workbook wb = WorkbookFactory.create(file)) {
            Map<String, Diver> divers = new HashMap<>();
            int numberOfSheets = wb.getNumberOfSheets();
            if (numberOfSheets == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = wb.getSheetAt(0);
            for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
                Row row = sheet.getRow(r);
                Diver diver = evalDiver(row);
                if (diver == null) {
                    continue;
                }
                PersonalCard card = diver.getCards().get(0);
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
                    PersonalCard newPrimaryCard = diver.getPrimaryPersonalCard();
                    if (newPrimaryCard != null) {
                        PersonalCard existingDiverPrimaryCard = existingDiver.getPrimaryPersonalCard();
                        if (existingDiverPrimaryCard == null) {
                            existingDiver.setPrimaryPersonalCard(newPrimaryCard);
                        }
                    }
                }
            }
            return divers.values();
        }
    }

    @SuppressWarnings("MagicNumber")
    @Nullable
    private static Diver evalDiver(Row row) {
        if (row == null || row.getCell(5) == null) {
            return null;
        }
        String email = StringUtil.correctSpaceCharAndTrim(row.getCell(5).getStringCellValue());
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        Diver diver = new Diver();
        //0
        diver.setFirstName(StringUtil.correctSpaceCharAndTrim(row.getCell(0).getStringCellValue()));
        //1
        diver.setLastName(StringUtil.correctSpaceCharAndTrim(row.getCell(1).getStringCellValue()));
        //2
        diver.setDob(row.getCell(2).getDateCellValue());

        //3
        PersonalCard card = new PersonalCard();
        List<PersonalCard> cards = new ArrayList<>();
        cards.add(card);
        diver.setCards(cards);
        Cell cell3 = row.getCell(3);
        if (cell3 != null) {
            card.setNumber(StringUtil.correctSpaceCharAndTrim(cell3.getStringCellValue()));
        }

        //4
        Cell cell4 = row.getCell(4);
        if (cell4 != null) {
            String instructorCardNumber = StringUtil.correctSpaceCharAndTrim(cell4.getStringCellValue());
            if (!StringUtil.isTrimmedEmpty(instructorCardNumber)) {
                Diver instructor = new Diver();
                instructor.setDiverType(DiverType.INSTRUCTOR);
                PersonalCard instructorCard = new PersonalCard();
                instructorCard.setCardType(PersonalCardType.NATIONAL);
                instructorCard.setDiverType(DiverType.INSTRUCTOR);
                instructorCard.setNumber(instructorCardNumber);
                List<PersonalCard> instructorCards = new ArrayList<>(1);
                instructorCards.add(instructorCard);
                instructor.setCards(instructorCards);
                diver.setInstructor(instructor);
            }
        }

        // 5
        diver.setEmail(email);

        //6
        Cell cell6 = row.getCell(6);
        String diverTypeStr = null;
        if (cell6 != null) {
            diverTypeStr = StringUtil.correctSpaceCharAndTrim(cell6.getStringCellValue());
        }
        DiverType diverType = DiverType.DIVER;
        if (!StringUtil.isTrimmedEmpty(diverTypeStr)
            && DiverType.INSTRUCTOR.name().equals(diverTypeStr.toUpperCase(Locale.ENGLISH))) {
            diverType = DiverType.INSTRUCTOR;
        }
        card.setDiverType(diverType);
        diver.setDiverType(diverType);

        //7
        Cell cell7 = row.getCell(7);
        String cardTypeStr = null;
        if (cell7 != null) {
            cardTypeStr = StringUtil.correctSpaceCharAndTrim(cell7.getStringCellValue());
        }
        setCardTypeFromStr(card, cardTypeStr);

        //8
        Cell cell8 = row.getCell(8);
        if (cell8 != null) {
            Integer value = getIntegerValue(cell8);
            if (value != null) {
                setDiverLevelFromInt(card, value);
                diver.setDiverLevel(card.getDiverLevel());
            }
        }

        //9 image reading is broken

        //10 reading hidden info - preferred primary card number
        Cell cell10 = row.getCell(10);
        if (cell10 != null) {
            Integer value = getIntegerValue(cell10);
            if (value != null) {
                if (value > 0) {
                    String primaryCardNumberStr = String.valueOf(value);
                    PersonalCard primaryCard = new PersonalCard();
                    primaryCard.setNumber(primaryCardNumberStr);
                    diver.setPrimaryPersonalCard(primaryCard);

                }
            }
        }

        return diver;
    }

    private static Integer getIntegerValue(Cell cell10) {
        Integer value = null;
        int cellType = cell10.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC:
                value = (int) cell10.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                String str = cell10.getStringCellValue();
                if (!StringUtil.isTrimmedEmpty(str)) {
                    value = Integer.parseInt(StringUtil.correctSpaceCharAndTrim(str));
                }
                break;
        }
        return value;
    }
}
