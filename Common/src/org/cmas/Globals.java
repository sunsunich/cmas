package org.cmas;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

interface GlobalsStaticContainer {

    int PRECISION = 12;
    int SCALE = 2;

    int LOWEST_EIGHT_BITS_MASK = 0xff;

    int MAX_LENGTH = 255;
    int HALF_MAX_LENGTH = 128;

    int SPORTS_CARD_NUMBER_MAX_LENGTH = 16;

    int VERY_BIG_MAX_LENGTH = 2048;

    int DB_PIC_MAX_BYTE_SIZE = 17000000;

    String DIRECTIVE_DATE_FORMAT = "dd.MM.yy";

    String DIRECTIVE_FULL_DATE_FORMAT = "dd.MM.yyyy";

    String DOCUMENT_DATE_FORMAT = "yyyy-MM-dd";

    String DOCUMENT_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";

    String TABLE_GRAPH_DATE_DISPLAY_FORMAT = "dd/MM/yy";

    String DIRECTIVE_DATE_TIME_DB_FORMAT = "yyyy.MM.dd";

    String TIME_FORMAT = "HH:mm";

    String SHORT_DATE_FORMAT = "dd/MM";

    String USER_NAME_REGEX = "[a-zA-Z0-9_@.]+";

    String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    String UTF_8_ENC = "UTF-8";

    String HTTP_PROTOCOL = "http";

    String PROFILE_ID_REQ_PARAM = "profileId";

    String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String MOBILE_DB_PASS = "oT98{7s%{7oQg$";

    String DEFAULT_PROFILE_NAME = "Я";

    String DTF = "dd/MM/yyyy";

    int ITEMS_AMOUNT_INT_DIGITS = 5;

    int PRICE_INT_DIGITS = 6;
    int PRICE_FRAC_DIGITS = 2;

    int SPEC_PRECISION = 7;
    int SPEC_SCALE = 3;
    int SPEC_MAX_LENGTH = 255;
    int SPEC_LIST_MAX_LENGTH = 512;

    int NAME_MAX_LENGTH = 512;

    int FULL_DESCR_LENGTH = 3000;

    long ONE_DAY_IN_MS = 1000L * 60L * 60L * 24L;

    long ONE_WEEK_IN_MS = 1000L * 60L * 60L * 24L * 7L;

    String EMAIL_REGEXP = ".+@.+\\..+";

    String PHONE_REGEXP = "[\\+0-9 ]+";

    BigDecimal HUNDRED = new BigDecimal(100);
    BigDecimal HUNDRED_PERCENT = new BigDecimal(100);
}

public final class Globals implements GlobalsStaticContainer {

    public static SimpleDateFormat getDocumentDateDisplayFormat() {
        return new SimpleDateFormat(DOCUMENT_DATE_DISPLAY_FORMAT);
    }

    public static SimpleDateFormat getDocumentDateFormat() {
        return new SimpleDateFormat(DOCUMENT_DATE_FORMAT);
    }

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(TIME_FORMAT);
    }

    public static SimpleDateFormat getShortDateFormat() {
        return new SimpleDateFormat(SHORT_DATE_FORMAT);
    }

    public static SimpleDateFormat getDTF() {
        return new SimpleDateFormat(DTF);
    }
}
