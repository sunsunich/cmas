package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.service.user.ProgressListener;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EgyptDiverXlsParserImpl.class);

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
                    String sheetName = sheet.getSheetName();
                    LOGGER.error("error reading diver's card:" + "sheetName:" + sheetName);
                    continue;
                }
                for (int r = 2; r < sheet.getPhysicalNumberOfRows(); r++) {
                    try {
                        Row row = sheet.getRow(r);
                        if (row == null) {
                            continue;
                        }
                        Diver diver = evalDiver(row);
                        if (diver == null) {
                            LOGGER.error("error reading diver: row number:" + r);
                            continue;
                        }
                        diver.setDiverType(globalCard.getDiverType());
                        diver.setDiverLevel(globalCard.getDiverLevel());
                        PersonalCard card = diver.getCards().get(0);
                        card.setDiverType(globalCard.getDiverType());
                        card.setDiverLevel(globalCard.getDiverLevel());
                        card.setCardType(globalCard.getCardType());
                        card.setFederationName(globalCard.getFederationName());

                        String key = diver.getFirstName() + ' ' + diver.getLastName() + ' ' + diver.getDob();
                        Diver existingDiver = divers.get(key);
                        if (existingDiver == null) {
                            divers.put(key, diver);
                        } else {
                            LOGGER.error("diver repetition:"
                                         + "cell number:"
                                         + row.getCell(0)
                                         + ", diver:"
                                         + existingDiver.getFirstName()
                                         + ", "
                                         + existingDiver.getLastName()
                                         + ", "
                                         + Globals.getDTF().format(existingDiver.getDob())
                                         + ", "
                                         + existingDiver.getDiverType()
                                         + ", "
                                         + existingDiver.getDiverLevel()
                                         + ",  new card:"
                                         + card.getDiverType()
                                         + ", "
                                         + card.getDiverLevel()
                            );
                            // use the email of the latest diver - helps a little to avoid duplicates
                            existingDiver.setEmail(diver.getEmail());
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
    private static Diver evalDiver(Row row) throws Exception {
        Diver diver = new Diver();
        Cell nameCell = row.getCell(1);
        if (nameCell == null || nameCell.getStringCellValue() == null) {
            return null;
        }
        String name = StringUtil.correctSpaceCharAndTrim(nameCell.getStringCellValue());
        int lastSpaceIndex = name.lastIndexOf(' ');
        if (lastSpaceIndex == -1) {
            return null;
        }
        diver.setFirstName(name.substring(0, lastSpaceIndex).trim());
        diver.setLastName(name.substring(lastSpaceIndex + 1).trim());

        Cell countryCell = row.getCell(2);
        if (countryCell != null) {
            Country country = new Country();
            country.setName(StringUtil.correctSpaceCharAndTrim(countryCell.getStringCellValue()));
            diver.setCountry(country);
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

        String email = StringUtil.correctSpaceCharAndTrim(row.getCell(4).getStringCellValue());
        diver.setEmail(email);

        String password = StringUtil.correctSpaceCharAndTrim(row.getCell(5).getStringCellValue());
        diver.setGeneratedPassword(password);

        Cell dobCell = row.getCell(6);
        Date dob;
        if (dobCell.getCellType() == Cell.CELL_TYPE_STRING) {
            dob = Globals.getDTF().parse(dobCell.getStringCellValue());
        } else {
            dob = dobCell.getDateCellValue();
        }
        diver.setDob(dob);
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
