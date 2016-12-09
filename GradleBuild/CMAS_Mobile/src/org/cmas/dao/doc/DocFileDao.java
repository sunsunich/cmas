package org.cmas.dao.doc;

import android.content.Context;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.doc.DocFile;

import java.io.File;
import java.util.List;

public interface DocFileDao {

    String save(Context context, DictionaryEntity document, DocFile docFile) throws Exception;

    void delete(Context context, String fileName);

    void deleteAllForDocument(Context context, DictionaryEntity document);

    List<File> getFiles(Context context, DictionaryEntity document);
}
