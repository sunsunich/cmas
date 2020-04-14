package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.service.user.ProgressListener;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.NotNull;
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
public class SingleTableDiverXlsParserImpl extends BaseDiverXlsParserImpl {

    @Override
    public Collection<Diver> getDivers(InputStream file, ProgressListener progressListener) throws Exception {
        return getDivers(file, progressListener, 0);
    }

    @NotNull
    protected Collection<Diver> getDivers(InputStream file, ProgressListener progressListener, int startingRow) throws Exception {
        try (Workbook wb = WorkbookFactory.create(file)) {
            int workDone = 0;
            int totalWork = evalTotalWork(wb);
            Map<String, Diver> divers = new HashMap<>();
            Sheet sheet = wb.getSheetAt(0);
            for (int r = startingRow; r < sheet.getPhysicalNumberOfRows(); r++) {
                try {
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
                    workDone++;
                    progressListener.updateProgress(workDone * 100 / totalWork);
                } catch (Exception e) {
                    throw new XlsParseException(String.valueOf(r + 1), e.getMessage(), e);
                }
            }
            return divers.values();
        }
    }

    @SuppressWarnings("MagicNumber")
    @Nullable
    protected Diver evalDiver(Row row) {
        if (row == null || row.getCell(5) == null) {
            return null;
        }
        String email = StringUtil.lowerCaseEmail(row.getCell(5).getStringCellValue());
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        Diver diver = new Diver();
        //0
        diver.setFirstName(StringUtil.correctSpaceCharAndTrim(row.getCell(0).getStringCellValue()));
        //1
        diver.setLastName(StringUtil.correctSpaceCharAndTrim(row.getCell(1).getStringCellValue()));
        //2
        diver.setDob(getDateCellValue(row.getCell(2)));

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
            setInstructor(diver, instructorCardNumber);
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
        setDiverLevelFromInt(card, getIntegerValue(row.getCell(8)));
        diver.setDiverLevel(card.getDiverLevel());

        //9 image reading is broken

        //10 reading hidden info - preferred primary card number
        Integer value10 = getIntegerValue(row.getCell(10));
        if (value10 != null) {
            if (value10 > 0) {
                String primaryCardNumberStr = String.valueOf(value10);
                PersonalCard primaryCard = new PersonalCard();
                primaryCard.setNumber(primaryCardNumberStr);
                diver.setPrimaryPersonalCard(primaryCard);

            }
        }

        return diver;
    }
}
