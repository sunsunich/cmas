package org.cmas.remote;

public final class ErrorCodes {

    private ErrorCodes() {
    }

    public static final String UNSUPPORTED_DEVICE_TYPE = "unsupported.device.type";
    public static final String ERROR_REGISTERING_DEVICE = "error.registering.device";
    public static final String ERROR_UNREGISTERING_DEVICE = "error.unregistering.device";
    public static final String ERROR_WHILE_SAVING_USER = "error.while.saving.user";

    public static final String ERROR = "error";
    public static final String TOTAL_IMAGE_SIZE_TOO_BIG = "validation.multipleImagesSize";

    public static final String USER_ALREADY_EXISTS = "user.already.exists";
    public static final String USER_IS_DELETED = "user.is.deleted";
    public static final String WRONG_PASSWORD = "wrong.password";
    public static final String IS_ALREADY_THE_OWNER_OF_PROFILE = "is.already.owner.of.profile";
    public static final String PROFILE_ALREADY_EXISTS = "profile.already.exists";
    public static final String IS_NOT_THE_OWNER_OF_PROFILE = "is.not.owner.of.profile";
    public static final String NO_SUCH_USER = "no.such.user";
    public static final String SESSION_EXPIRED = "session.expired";
    public static final String INTERNET_IS_UNAVAILABLE = "net.unavailable";
    public static final String EMAIL_ALREADY_EXISTS="validation.emailExists";
}
