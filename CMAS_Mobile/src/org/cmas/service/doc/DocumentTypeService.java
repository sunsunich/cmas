package org.cmas.service.doc;

import android.content.Context;
import org.cmas.entities.doc.DocumentType;

import java.util.List;

public interface DocumentTypeService {

    List<DocumentType> getAllNoDeleted(Context context);

    DocumentType getById(Context context, long id);
}
