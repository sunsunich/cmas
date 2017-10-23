package org.cmas.presentation.validator;

import org.cmas.util.StringUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public final class ValidatorUtils {

    private ValidatorUtils() {
    }

    public static <E extends Enum<E>> void validateEnum(Errors errors, final String value, final Class<E> enumClass, String fieldName, String validationMessage) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        Method method = enumClass.getMethod("valueOf", String.class);
                        method.invoke(null, value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateInteger(Errors errors, final String value, String fieldName, String validationMessage) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        Integer.parseInt(value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateDouble(Errors errors, final String value, String fieldName, String validationMessage) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        Double.parseDouble(value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateLong(Errors errors, final String value, String fieldName, String validationMessage) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        Long.parseLong(value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateBigDecimal(Errors errors, final String value, String fieldName, String validationMessage) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        //noinspection ResultOfObjectAllocationIgnored
                        new BigDecimal(value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateDate(Errors errors, final String value, String fieldName, String validationMessage, final SimpleDateFormat dateFormat) {
        validateWithAction(errors
                , new ValidationAction() {
                    @Override
                    public void doActionValidatingAction() throws Exception {
                        //noinspection ResultOfObjectAllocationIgnored
                        dateFormat.parse(value);
                    }
                }
                , value, fieldName, validationMessage
        );
    }

    public static void validateBoolean(Errors errors, String value, String fieldName, String validationMessage) {
        if (!Boolean.parseBoolean(value)) {
            errors.rejectValue(fieldName, validationMessage);
        }
    }

    public static void validateWithAction(Errors errors, ValidationAction validationAction, String value, String fieldName, String validationMessage) {
        if (!StringUtil.isTrimmedEmpty(value)) {
            try {
                validationAction.doActionValidatingAction();
            } catch (Exception e) {
                errors.rejectValue(fieldName, validationMessage);
            }
        }
    }

    public static String[] getAllErrorCodes(Errors errors) {
        if (errors.hasErrors()) {
            String[] codes = new String[errors.getErrorCount()];
            List allErrors = errors.getAllErrors();
            for (int i = 0; i < allErrors.size(); i++) {
                Object error = allErrors.get(i);
                if (error instanceof DefaultMessageSourceResolvable) {
                    codes[i] = ((DefaultMessageSourceResolvable) error).getCode();
                }
            }
            return codes;
        } else {
            return new String[]{};
        }
    }
}
