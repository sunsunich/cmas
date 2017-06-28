package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.annotations.Expose;

/**
 * Created on Jun 26, 2017
 *
 * @author Alexander Petukhov
 */
public class XlsParseProgressJsonBean {
    @Expose
    private int progressPercent;

    public XlsParseProgressJsonBean() {
    }

    public XlsParseProgressJsonBean(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }
}
