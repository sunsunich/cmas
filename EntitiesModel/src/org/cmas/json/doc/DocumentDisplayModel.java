package org.cmas.json.doc;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.doc.Document;

import java.util.List;

public class DocumentDisplayModel {

    @Expose
    private List<DocumentViewerDisplayModel> creators;

    @Expose
    private List<Document> docs;

    public DocumentDisplayModel() {
    }

    public DocumentDisplayModel(List<DocumentViewerDisplayModel> creators, List<Document> docs) {
        this.creators = creators;
        this.docs = docs;
    }

    public List<DocumentViewerDisplayModel> getCreators() {
        return creators;
    }

    public void setCreators(List<DocumentViewerDisplayModel> creators) {
        this.creators = creators;
    }

    public List<Document> getDocs() {
        return docs;
    }

    public void setDocs(List<Document> docs) {
        this.docs = docs;
    }
}
