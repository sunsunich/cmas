package org.cmas.entities.doc;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "document_types")
public class DocumentType extends DictionaryEntity {

    private static final long serialVersionUID = 1998213133907940157L;

    @Column(unique = true)
    @Expose
    private String localName;

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
