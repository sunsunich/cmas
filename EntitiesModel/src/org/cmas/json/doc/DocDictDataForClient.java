package org.cmas.json.doc;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.doc.DocumentType;

import java.util.List;

public class DocDictDataForClient {

    @Expose
    private List<DocumentType> docTypes;

    public DocDictDataForClient() {
    }

    public DocDictDataForClient(List<DocumentType> docTypes) {
        this.docTypes = docTypes;
    }

    public List<DocumentType> getDocTypes() {
        return docTypes;
    }

    public void setDocTypes(List<DocumentType> docTypes) {
        this.docTypes = docTypes;
    }
}
