package org.cmas.presentation.model;

import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class FeedbackFormObject implements Validatable {

    public enum FeedbackType {
        GENERAL, DIVE_SPOT, LOGBOOK_ENTRY
    }

    @NotEmpty(message = "validation.emptyField")
    private String feedbackType;

    private String diveSpotId;

    private String logbookEntryId;

    @NotEmpty(message = "validation.emptyField")
    @Length(max = Globals.VERY_BIG_MAX_LENGTH, message = "validation.maxLength")
    private String text;

    private MultipartFile image1;

    private MultipartFile image2;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors,
                                    feedbackType,
                                    FeedbackType.class,
                                    "feedbackType",
                                    "validation.incorrectField");
        if (errors.hasErrors()) {
            return;
        }
        FeedbackType feedbackTypeEnum = FeedbackType.valueOf(feedbackType);
        switch (feedbackTypeEnum) {
            case GENERAL:
                break;
            case DIVE_SPOT:
                if (StringUtil.isTrimmedEmpty(diveSpotId)) {
                    errors.rejectValue("diveSpotId", "validation.emptyField");
                } else {
                    ValidatorUtils.validateLong(errors, diveSpotId, "diveSpotId", "validation.incorrectField");
                }
                break;
            case LOGBOOK_ENTRY:
                if (StringUtil.isTrimmedEmpty(logbookEntryId)) {
                    errors.rejectValue("logbookEntryId", "validation.emptyField");
                } else {
                    ValidatorUtils.validateLong(errors, logbookEntryId, "logbookEntryId", "validation.incorrectField");
                }
                break;
        }
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getDiveSpotId() {
        return diveSpotId;
    }

    public void setDiveSpotId(String diveSpotId) {
        this.diveSpotId = diveSpotId;
    }

    public String getLogbookEntryId() {
        return logbookEntryId;
    }

    public void setLogbookEntryId(String logbookEntryId) {
        this.logbookEntryId = logbookEntryId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultipartFile getImage1() {
        return image1;
    }

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }

    public MultipartFile getImage2() {
        return image2;
    }

    public void setImage2(MultipartFile image2) {
        this.image2 = image2;
    }
}
