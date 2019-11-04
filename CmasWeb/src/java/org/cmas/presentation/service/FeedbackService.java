package org.cmas.presentation.service;

import org.cmas.entities.FeedbackItem;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.model.FeedbackFormObject;
import org.springframework.validation.Errors;

/**
 * Created on Oct 07, 2019
 *
 * @author Alexander Petukhov
 */
public interface FeedbackService {

    void processFeedback(Errors result, FeedbackFormObject feedbackFormObject, Diver diver, FeedbackItem feedbackItem);
}