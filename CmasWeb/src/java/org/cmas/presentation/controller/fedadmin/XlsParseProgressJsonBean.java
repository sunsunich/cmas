package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.annotations.Expose;
import org.cmas.presentation.service.user.UploadDiversTaskStatus;

/**
 * Created on Jun 26, 2017
 *
 * @author Alexander Petukhov
 */
public class XlsParseProgressJsonBean {

    @Expose
    private String error;

    @Expose
    private XlsParseErrorJsonBean parseError;

    @Expose
    private UploadDiversTaskStatus taskStatus;

    @Expose
    private int progressPercent;

    @Expose
    private int diversProcessed;

    public XlsParseProgressJsonBean() {
    }

    public XlsParseProgressJsonBean(UploadDiversTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public XlsParseProgressJsonBean(UploadDiversTaskStatus taskStatus,
                                    int progressPercent,
                                    int diversProcessed,
                                    String error,
                                    XlsParseErrorJsonBean parseError
    ) {
        this.error = error;
        this.parseError = parseError;
        this.taskStatus = taskStatus;
        this.progressPercent = progressPercent;
        this.diversProcessed = diversProcessed;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public XlsParseErrorJsonBean getParseError() {
        return parseError;
    }

    public void setParseError(XlsParseErrorJsonBean parseError) {
        this.parseError = parseError;
    }

    public UploadDiversTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(UploadDiversTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getDiversProcessed() {
        return diversProcessed;
    }

    public void setDiversProcessed(int diversProcessed) {
        this.diversProcessed = diversProcessed;
    }
}
