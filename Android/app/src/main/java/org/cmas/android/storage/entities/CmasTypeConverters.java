package org.cmas.android.storage.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;
import org.cmas.entities.DeviceType;
import org.cmas.entities.Gender;
import org.cmas.entities.UserFileType;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;

import java.util.Date;

public class CmasTypeConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Gender genderFromString(String value) {
        return enumFromString(Gender.class, value);
    }

    @TypeConverter
    public static String stringFromGender(Gender value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static DeviceType deviceTypeFromString(String value) {
        return enumFromString(DeviceType.class, value);
    }

    @TypeConverter
    public static String stringFromDeviceType(DeviceType value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static UserFileType userFileTypeFromString(String value) {
        return enumFromString(UserFileType.class, value);
    }

    @TypeConverter
    public static String stringFromUserFileType(UserFileType value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static AreaOfInterest areaOfInterestFromString(String value) {
        return enumFromString(AreaOfInterest.class, value);
    }

    @TypeConverter
    public static String stringFromAreaOfInterest(AreaOfInterest value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static DiverLevel diverLevelFromString(String value) {
        return enumFromString(DiverLevel.class, value);
    }

    @TypeConverter
    public static String stringFromDiverLevel(DiverLevel value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static DiverType diverTypeFromString(String value) {
        return enumFromString(DiverType.class, value);
    }

    @TypeConverter
    public static String stringFromDiverType(DiverType value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static DiverRegistrationStatus diverRegistrationStatusFromString(String value) {
        return enumFromString(DiverRegistrationStatus.class, value);
    }

    @TypeConverter
    public static String stringFromDiverRegistrationStatus(DiverRegistrationStatus value) {
        return stringFromEnum(value);
    }

    @TypeConverter
    public static PersonalCardType personalCardTypeFromString(String value) {
        return enumFromString(PersonalCardType.class, value);
    }

    @TypeConverter
    public static String stringFromPersonalCardType(PersonalCardType value) {
        return stringFromEnum(value);
    }

    @Nullable
    private static <T extends Enum<T>> T enumFromString(@NonNull Class<T> enumClass, @Nullable String value) {
        return value == null ? null : T.valueOf(enumClass, value);
    }

    @Nullable
    private static <T extends Enum<T>> String stringFromEnum(@Nullable T value) {
        return value == null ? null : value.name();
    }
}
