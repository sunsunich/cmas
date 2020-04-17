package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cmas.entities.Country;
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
import java.util.Locale;
import java.util.Map;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("MagicCharacter")
public class EgyptDiverXlsParserImpl extends BaseDiverXlsParserImpl {

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
                for (int r = 4; r < sheet.getPhysicalNumberOfRows(); r++) {
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

                        String key = diver.getFirstName() + ' ' + diver.getLastName();
                        Diver existingDiver = divers.get(key);
                        if (existingDiver == null) {
                            divers.put(key, diver);
                        } else {
                            System.out.println("diver repetition:"
                                               + diver.getFirstName()
                                               + ' '
                                               + diver.getLastName()
                                               + ' '
                                               + diver.getDiverLevel());
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
        Diver diver = new Diver();
        String name = StringUtil.correctSpaceCharAndTrim(row.getCell(1).getStringCellValue());
        int firstSpaceIndex = name.indexOf(' ');
        if (firstSpaceIndex == -1) {
            return null;
        }
        diver.setFirstName(name.substring(0, firstSpaceIndex));
        diver.setLastName(name.substring(firstSpaceIndex + 1));

        Cell countryCell = row.getCell(2);
        if (countryCell != null) {
            Country country = new Country();
            country.setName(StringUtil.correctSpaceCharAndTrim(countryCell.getStringCellValue()));
            diver.setCountry(country);
        }

        Cell instNumberCell = row.getCell(6);
        if (instNumberCell != null) {
            String instructorCardNumber = StringUtil.correctSpaceCharAndTrim(instNumberCell.getStringCellValue());
            setInstructor(diver, instructorCardNumber);
        }

        Cell cardNumberCell = row.getCell(3);
        if (cardNumberCell != null) {
            PersonalCard card = new PersonalCard();
            if (cardNumberCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                card.setNumber(String.valueOf((int) cardNumberCell.getNumericCellValue()));
            } else {
                card.setNumber(StringUtil.correctSpaceCharAndTrim(cardNumberCell.getStringCellValue()));
            }
            List<PersonalCard> cards = new ArrayList<>();
            cards.add(card);
            diver.setCards(cards);
        }
        return diver;
    }

    @Nullable
    private static PersonalCard evalDiverTypeLevel(Sheet sheet) {
        PersonalCard card = new PersonalCard();
        String sheetName = sheet.getSheetName();
        if (sheetName.toLowerCase(Locale.ENGLISH).contains("ins")) {
            card.setDiverType(DiverType.INSTRUCTOR);
        } else {
            card.setDiverType(DiverType.DIVER);
        }
        // national card
        setCardTypeFromStr(card, "");
        setDiverLevelFromInt(card, Integer.parseInt(sheetName.substring(sheetName.length() - 1)));
        return card;
    }
}
