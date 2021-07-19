package org.cmas.backend.xls;

import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.service.user.ProgressListener;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * Created on Apr 28, 2016
 *
 * @author Alexander Petukhov
 */
public class EgyptDiverXlsParserImplTest {

    @Test
    public void test() {
        EgyptDiverXlsParserImpl sut = new EgyptDiverXlsParserImpl();
        try {
            Collection<Diver> divers = sut.getDivers(new File(
                    "/Users/sunsunich/Documents/cmas/federations/Egypt/2021/CMAS Data.xlsx"
            ), new ProgressListener() {
                @Override
                public void updateProgress(int newPercentValue) {

                }
            });
            //  Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/BookRusDivers.xlsx"));
            //   Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/Arzhanova.xlsx"));
            System.out.println("Amount of divers = " + divers.size());
            for (Diver diver : divers) {
                System.out.println(diver.getFirstName()
                                   + ' '
                                   + diver.getLastName()
                                   + ' '
                                   + diver.getDob()
                                   + ' '
                                   + diver.getDiverType()
                                   + ' '
                                   + diver.getDiverLevel()
                                   + " from "
                                   + diver.getCountry().getName());
                for (PersonalCard card : diver.getCards()) {
                    System.out.println("   "
                                       + card.getDiverType()
                                       + " "
                                       + card.getDiverLevel()
                                       + " "
                                       + card.getNumber());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
