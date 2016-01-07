package org.cmas.entities.doc;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "document_files")
public class DocFile extends DictionaryEntity {

    //set by server

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
}