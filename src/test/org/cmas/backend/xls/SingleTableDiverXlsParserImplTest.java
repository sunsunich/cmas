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
public class SingleTableDiverXlsParserImplTest {

    @Test
    public void test() {
        SingleTableDiverXlsParserImpl sut = new SingleTableDiverXlsParserImpl();
        try {
             //Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/BookPortDiversOneTable.xlsx"));
             Collection<Diver> divers = sut.getDivers(new File("/Users/sunsunich/Documents/cmas/federations/DataCMAS1_ire.xlsx"));
            for (Diver diver : divers) {
                System.out.println(diver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
