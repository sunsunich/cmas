package org.cmas.backend.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.service.user.ProgressListener;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("MagicCharacter")
public class IranDiverXlsParserImpl extends SingleTableDiverXlsParserImpl {

    @Override
    public Collection<Diver> getDivers(InputStream file, ProgressListener progressListener) throws Exception {
        return getDivers(file, progressListener, 1);
    }

    @SuppressWarnings("MagicNumber")
    @Nullable
    @Override
    protected Diver evalDiver(Row row) {
        String email = StringUtil.correctSpaceCharAndTrim(row.getCell(2).getStringCellValue());
        if (StringUtil.isTrimmedEmpty(email)) {
            return null;
        }

        Diver diver = new Diver();
        // 0
        diver.setFirstName(StringUtil.correctSpaceCharAndTrim(row.getCell(0).getStringCellValue()));
        // 1
        diver.setLastName(StringUtil.correctSpaceCharAndTrim(row.getCell(1).getStringCellValue()));

        diver.setDob(null);

        // 2
        diver.setEmail(email);

        // 3
        PersonalCard card = new PersonalCard();
        List<PersonalCard> cards = new ArrayList<>();
        cards.add(card);
        diver.setCards(cards);
        Cell cell3 = row.getCell(3);
        if (cell3 != null) {
            String[] cardStrs = StringUtil.correctSpaceCharAndTrim(cell3.getStringCellValue()).split(" ");

            setDiverLevelFromInt(card, Integer.parseInt(cardStrs[1]));
            diver.setDiverLevel(card.getDiverLevel());

            String diverTypeStr = cardStrs[3];
            DiverType diverType = DiverType.DIVER;
            if (!StringUtil.isTrimmedEmpty(diverTypeStr)
                && DiverType.INSTRUCTOR.name().equals(diverTypeStr.toUpperCase(Locale.ENGLISH))) {
                diverType = DiverType.INSTRUCTOR;
            }
            card.setCardType(PersonalCardType.NATIONAL);
            card.setDiverType(diverType);
            diver.setDiverType(diverType);
        }

        return diver;
    }
}
