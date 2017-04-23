package org.cmas.backend.xls;

import org.cmas.entities.diver.Diver;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * Created on Apr 28, 2016
 *
 * @author Alexander Petukhov
 */
public class RusDiverXlsParserImplTest {

    @Test
    public void test() {
        RusDiverXlsParserImpl sut = new RusDiverXlsParserImpl();
        try {
             Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/RusInstructor.xlsx"));
          //  Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/BookRusDivers.xlsx"));
         //   Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/Arzhanova.xlsx"));
            for (Diver diver : divers) {
                System.out.println(diver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
