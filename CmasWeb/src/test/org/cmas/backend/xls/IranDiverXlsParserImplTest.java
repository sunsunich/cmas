package org.cmas.backend.xls;

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
public class IranDiverXlsParserImplTest {

    @Test
    public void test() {
        IranDiverXlsParserImpl sut = new IranDiverXlsParserImpl();
        try {
            Collection<Diver> divers = sut.getDivers(new File(
                    "/Users/sunsunich/Documents/cmas/federations/Iran/DataBase.xlsx"
            ), new ProgressListener() {
                @Override
                public void updateProgress(int newPercentValue) {

                }
            });
           for (Diver diver : divers) {
                System.out.println(diver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
