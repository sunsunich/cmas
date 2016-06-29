package org.cmas.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created on Jun 23, 2016
 *
 * @author Alexander Petukhov
 */
public class StringUtilTest {

    @Test
    public void testAddLeadingZerosDecimal() {
        int maxDigits = 6;
        double maxNumber = StrictMath.pow(10.0, (double) maxDigits);
        for (int i = 0; (double) i < maxNumber; i++) {
            String s = StringUtil.addLeadingZerosDecimal(maxDigits, i);
            Assert.assertEquals((long) maxDigits, (long) s.length());
            Assert.assertEquals((long) Integer.parseInt(s), (long) i);
        }
        try {
            StringUtil.addLeadingZerosDecimal(maxDigits, (int)maxNumber);
        }
        catch (IllegalArgumentException ignored){
            Assert.assertTrue(true);
        }
        try {
            StringUtil.addLeadingZerosDecimal(maxDigits, (int)maxNumber + 1);
        }
        catch (IllegalArgumentException ignored){
            Assert.assertTrue(true);
        }
    }
}
