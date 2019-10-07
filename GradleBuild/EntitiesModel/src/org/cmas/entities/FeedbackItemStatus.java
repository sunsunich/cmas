package org.cmas.entities;

/**
 * Created on May 27, 2019
 *
 * @author Alexander Petukhov
 */
public enum FeedbackItemStatus {

    NEW, RESOLVED, DECLINED;

    public String getName() {
        return name();
    }
}
