package org.cmas.dao.doc;

import android.content.Context;
import org.cmas.entities.doc.DocFile;
import org.cmas.entities.doc.Document;

import java.io.File;
import java.util.List;

public interface DocFileDao {

    String save(Context context, Document document, DocFile docFile) throws Exception;

    void delete(Context context, String fileName);

    void deleteAllForDocument(Context context, Document document);

    List<File> getFiles(Context context, Document document);
}
