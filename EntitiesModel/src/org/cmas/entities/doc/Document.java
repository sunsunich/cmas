package org.cmas.entities.doc;


import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.sport.Sportsman;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "documents")
public class Document extends DictionaryEntity {

    private static final long serialVersionUID = 4716301040129778882L;

    //nullable
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = DocumentType.class)
    private DocumentType documentType;

    @Expose
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Sportsman.class)
    private Sportsman sportsman;

    @Expose
    @Column(nullable = false)
    private Date dateCreation;

    @Expose
    @Column(length = Globals.VERY_BIG_MAX_LENGTH)
    private String description;

    @Expose
    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER)
    private List<DocFile> files;


    //used at mobile only
    @Expose
    @Transient
    private long typeId;

    //end used at mobile only

    public Document() {
        dateCreation = new Date();
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
        if (documentType != null) {
            typeId = documentType.getId();
        }
    }

    public Sportsman getSportsman() {
        return sportsman;
    }

    public void setSportsman(Sportsman sportsman) {
        this.sportsman = sportsman;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DocFile> getFiles() {
        return files;
    }

    public void setFiles(List<DocFile> files) {
        this.files = files;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }
}
