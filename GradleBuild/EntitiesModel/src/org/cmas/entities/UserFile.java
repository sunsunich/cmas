package org.cmas.entities;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "user_files")
public class UserFile extends DictionaryEntity {

    @Expose
    @Column(nullable = false)
    private Date dateCreation;

    @Expose
    @Column(nullable = false)
    private Date dateEdit;

    @Expose
    @ManyToOne(optional = false)
    private Diver creator;

    @Expose
    @Column(nullable = false)
    private String fileUrl;

    @Expose
    @Column
    private String mimeType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserFileType userFileType;

    public UserFileType getUserFileType() {
        return userFileType;
    }

    public void setUserFileType(UserFileType userFileType) {
        this.userFileType = userFileType;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public Diver getCreator() {
        return creator;
    }

    public void setCreator(Diver creator) {
        this.creator = creator;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
