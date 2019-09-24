package org.cmas.entities;

/**
 * Created on May 27, 2019
 *
 * @author Alexander Petukhov
 */
public enum ErrorReportStatus {

    NEW, RESOLVED, DECLINED;

    public String getName() {
        return name();
    }
}
