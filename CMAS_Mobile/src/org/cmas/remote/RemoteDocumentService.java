package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.doc.Document;
import org.cmas.json.EntityEditReply;
import org.cmas.json.doc.DocCreateReply;
import org.cmas.json.doc.DocumentDisplayModel;

public interface RemoteDocumentService extends BaseRemoteService {

    Pair<DocCreateReply, String> addNewDoc(
            Context context,
            Document document
    ) throws Exception;

    Pair<EntityEditReply, String> editDoc(
            Context context,
            Document document
    ) throws Exception;

    Pair<EntityEditReply, String> deleteDoc(
            Context context,
            long documentId
    ) throws Exception;

    Pair<DocumentDisplayModel, String> getUserDocs(
            Context context,
            long maxVersion
    ) throws Exception;
}
