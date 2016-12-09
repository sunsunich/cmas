package org.cmas.presentation.model.logbook;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.logbook.DiveScore;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidationAction;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.hibernate.validator.Digits;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

/**
 * Created on Jul 02, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookEntryFormObject implements Validatable {

    private String logbookEntryId;

    @Digits(integerDigits = 20, message = "validation.incorrectNumber")
    @NotEmpty(message = "validation.emptyField")
    private String spotId;

    @Digits(integerDigits = 3, message = "validation.incorrectNumber")
    @NotEmpty(message = "validation.emptyField")
    private String duration;

    @Digits(integerDigits = 4, message = "validation.incorrectNumber")
    @NotEmpty(message = "validation.emptyField")
    private String depth;

    @NotEmpty(message = "validation.emptyField")
    private String diveDate;

    @NotEmpty(message = "validation.emptyField")
    private String score;

    @NotEmpty(message = "validation.emptyField")
    private String visibility;

    @Length(max = Globals.VERY_BIG_MAX_LENGTH, message = "validation.maxLength")
    private String note;

    private String instructorId;

    private String buddiesIds;

    private String photo;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDate(errors, diveDate, "diveDate", "validation.incorrectDate", Globals.getDTF());
        ValidatorUtils.validateEnum(errors, score, DiveScore.class, "score", "validation.incorrectField");
        ValidatorUtils.validateEnum(errors,
                                    visibility,
                                    LogbookVisibility.class,
                                    "visibility",
                                    "validation.incorrectField");
        if(!StringUtil.isTrimmedEmpty(buddiesIds)) {
            ValidatorUtils.validateWithAction(errors,
                                              new ValidationAction() {
                                                  @Override
                                                  public void doActionValidatingAction() throws Exception {
                                                      new Gson().fromJson(buddiesIds, Globals.LONG_LIST_TYPE);
                                                  }
                                              }, buddiesIds, "buddiesIds", "validation.incorrectField"
            );
        }
        if(!StringUtil.isTrimmedEmpty(photo)) {
            ValidatorUtils.validateWithAction(errors,
                                              new ValidationAction() {
                                                  @Override
                                                  public void doActionValidatingAction() throws Exception {
                                                      Base64Coder.decode(photo);
                                                  }
                                              }, photo, "photo", "validation.incorrectField"
            );
        }
    }

    public String getLogbookEntryId() {
        return logbookEntryId;
    }

    public void setLogbookEntryId(String logbookEntryId) {
        this.logbookEntryId = logbookEntryId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getDiveDate() {
        return diveDate;
    }

    public void setDiveDate(String diveDate) {
        this.diveDate = diveDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getBuddiesIds() {
        return buddiesIds;
    }

    public void setBuddiesIds(String buddiesIds) {
        this.buddiesIds = buddiesIds;
    }
}
