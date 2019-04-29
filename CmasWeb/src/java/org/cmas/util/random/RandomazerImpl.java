package org.cmas.util.random;

import org.jetbrains.annotations.NonNls;

import java.security.SecureRandom;

public class RandomazerImpl implements Randomazer {

    private static final SecureRandom RND = new SecureRandom();

    @Override
    public String generateRandomStringByUniqueId(@NonNls long id, int randomPartLength) {

        String externalInvoiceNumberBeg = genRandom(randomPartLength);
        String externalInvoiceNumberEnd = genRandom(randomPartLength);
        return externalInvoiceNumberBeg.substring(0, randomPartLength)
               + id
               + externalInvoiceNumberEnd.substring(0, randomPartLength);
    }

    private static String genRandom(int minLength) {
        long minNumber = 10L * (long) (minLength - 1);
        long number = Math.abs(RND.nextLong());
        if (number > Long.MAX_VALUE - minNumber) {
            return String.valueOf(number);
        } else {
            return String.valueOf(minNumber + number);
        }
    }
}
