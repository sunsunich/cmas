package org.cmas.entities.elearning;

public enum ElearningTokenStatus {
    NOT_ASSIGNED, // just uploaded to DB
    ASSIGNED,     // assigned to diver
    CLICKED,      // user clicked on registration
    USED          // cannot set now, because e-learning platform does not provide feedback
}
