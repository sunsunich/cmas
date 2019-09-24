package org.cmas;

import com.google.myjson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"MultipleTopLevelClassesInFile", "ClassNameDiffersFromFileName"})
interface GlobalsStaticContainer {

    int PRECISION = 12;
    int SCALE = 2;

    int LOWEST_EIGHT_BITS_MASK = 0xff;

    int MAX_LENGTH = 255;
    int HALF_MAX_LENGTH = 128;

    int SPORTS_CARD_NUMBER_MAX_LENGTH = 16;

    int VERY_BIG_MAX_LENGTH = 2048;

    int DB_PIC_MAX_BYTE_SIZE = 17000000;

    String DOCUMENT_DATE_FORMAT = "yyyy-MM-dd";

    String TIME_FORMAT = "HH:mm";

    String SHORT_DATE_FORMAT = "dd/MM";

    String USER_NAME_REGEX = "[a-zA-Z0-9_@.]+";

    String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    String UTF_8_ENC = "UTF-8";

    String UTF_8_CHARSET = "utf-8";

    String HTTP_PROTOCOL = "http";

    String PROFILE_ID_REQ_PARAM = "profileId";

    String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String MOBILE_DB_PASS = "oT98{7s%{7oQg$";

    String DTF = "dd/MM/yyyy";
    String FEED_DTF = "dd MMM yyyy, HH:mm";
    String DTF_WEB_CONTROLS = "dd/MM/yyyy HH:mm";
    String FULL_DTF = "yyyy-MM-dd'T'HH:mm:ss.sssX";

    int PRICE_INT_DIGITS = 6;
    int PRICE_FRAC_DIGITS = 2;

    int NAME_MAX_LENGTH = 512;

    int FULL_DESCR_LENGTH = 3000;

    long ONE_DAY_IN_MS = 1000L * 60L * 60L * 24L;

    long ONE_WEEK_IN_MS = ONE_DAY_IN_MS * 7L;

    String SIMPLE_EMAIL_REGEXP = ".+@.+\\..+";

    String PHONE_REGEXP = "[\\+0-9 ]+";

    BigDecimal HUNDRED = new BigDecimal(100);
    BigDecimal HUNDRED_PERCENT = new BigDecimal(100);

    double DIVE_SPOT_DELTA_DEGREES = 0.0005; //50 meters
    double HALF_DIVE_SPOT_DELTA_DEGREES = DIVE_SPOT_DELTA_DEGREES / 2.0;

    int FAST_SEARCH_MIN_INPUT = 3;
    int FAST_SEARCH_MAX_RESULT = 10;
    int ADVANCED_SEARCH_MAX_RESULT = 20;

    long MAX_IMAGE_SIZE = 10L * 1024L * 1024L;

    long CMAS_LICENCE_PAID_FEATURE_DB_ID = 1L;
    long GOLD_MEMBERSHIP_PAID_FEATURE_DB_ID = 2L;
}

public final class Globals implements GlobalsStaticContainer {

    public static SimpleDateFormat getDocumentDateFormat() {
        return new SimpleDateFormat(DOCUMENT_DATE_FORMAT);
    }

    public static SimpleDateFormat getDTF() {
        return new SimpleDateFormat(DTF);
    }

    public static SimpleDateFormat getFullDTF() {
        return new SimpleDateFormat(FULL_DTF);
    }

    public static SimpleDateFormat getDTFWebControls() {
        return new SimpleDateFormat(DTF_WEB_CONTROLS);
    }

    @SuppressWarnings("EmptyClass")
    public static final class LongListTypeToken extends TypeToken<List<Long>> {
    }

    public static final Type LONG_LIST_TYPE = new LongListTypeToken().getType();

    public static long getMsInYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return (long) calendar.getActualMaximum(Calendar.DAY_OF_YEAR) * Globals.ONE_DAY_IN_MS;
    }
}
