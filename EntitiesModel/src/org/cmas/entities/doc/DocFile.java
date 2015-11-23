package org.cmas.entities.doc;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.*;

@Entity
@Table(name = "document_files")
public class DocFile extends DictionaryEntity {

    //set by server
    @ManyToOne
    private Document document;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(length = Globals.DB_PIC_MAX_BYTE_SIZE)
    private byte[] fileBytes;

    @Expose
    private String ext;

    //used for transport only
//    @Expose
//    @Transient
//    private String file;

    //used at mobile device only
    @Transient
    private long documentId;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
        if (document != null) {
            documentId = document.getId();
        }
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

//    public String getFile() {
//        return file;
//    }
//
//    public void setFile(String file) {
//        this.file = file;
//    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }
}
