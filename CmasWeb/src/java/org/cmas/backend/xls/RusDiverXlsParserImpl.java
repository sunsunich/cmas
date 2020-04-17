package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.service.user.ProgressListener;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("MagicCharacter")
public class RusDiverXlsParserImpl extends BaseDiverXlsParserImpl {

    @Override
    public Collection<Diver> getDivers(InputStream file, ProgressListener progressListener) throws Exception {
        try (Workbook wb = WorkbookFactory.create(file)) {
            int workDone = 0;
            int totalWork = evalTotalWork(wb);
            Map<String, Diver> divers = new HashMap<>();
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                PersonalCard globalCard = evalDiverTypeLevel(sheet);
                if (globalCard == null) {
                    continue;
                }
                for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
                    try {
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
                        }
                        workDone++;
                        progressListener.updateProgress(workDone * 100 / totalWork);
                    } catch (Exception e) {
                        String sheetName = sheet.getSheetName() == null ? "" : sheet.getSheetName();
                        throw new XlsParseException(
                                (r + 1) + ", sheet number " + (i + 1) + ", sheet name " + sheetName,
                                e.getMessage(),
                                e);
                    }
                }
            }
            return divers.values();
        }
    }

    @Nullable
    private static Diver evalDiver(Row row) {
        if (row == null || row.getCell(5) == null) {
            return null;
        }
        Diver diver = new Diver();
        String email = StringUtil.lowerCaseEmail(row.getCell(5).getStringCellValue());
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        diver.setFirstName(row.getCell(1).getStringCellValue());
        diver.setLastName(row.getCell(0).getStringCellValue());
        diver.setDob(getDateCellValue(row.getCell(2)));
        diver.setEmail(email);

        Cell cell4 = row.getCell(4);
        if (cell4 != null) {
            String instructorCardNumber = StringUtil.correctSpaceCharAndTrim(cell4.getStringCellValue());
            setInstructor(diver, instructorCardNumber);
        }

        Cell cell3 = row.getCell(3);
        if (cell3 != null) {
            PersonalCard card = new PersonalCard();
            card.setNumber(StringUtil.correctSpaceCharAndTrim(cell3.getStringCellValue()));
            List<PersonalCard> cards = new ArrayList<>();
            cards.add(card);
            diver.setCards(cards);
        }
        return diver;
    }

    @Nullable
    private static PersonalCard evalDiverTypeLevel(Sheet sheet) {
        PersonalCard card = new PersonalCard();
        Cell diverTypeCell = sheet.getRow(0).getCell(0);
        Cell cardTypeCell = sheet.getRow(0).getCell(1);
        Cell diverLevelCell = sheet.getRow(0).getCell(2);
        if (diverTypeCell != null
            && DiverType.INSTRUCTOR.name()
                                   .equals(StringUtil.correctSpaceCharAndTrim(diverTypeCell.getStringCellValue()))) {
            card.setDiverType(DiverType.INSTRUCTOR);
        } else {
            card.setDiverType(DiverType.DIVER);
        }
        if (cardTypeCell != null) {
            setCardTypeFromStr(card, StringUtil.correctSpaceCharAndTrim(cardTypeCell.getStringCellValue()));
        }
        setDiverLevelFromInt(card, getIntegerValue(diverLevelCell));
        return card;
    }
}
