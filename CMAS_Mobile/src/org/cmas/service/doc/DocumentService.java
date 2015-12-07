package org.cmas.service.doc;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import org.cmas.entities.doc.Document;
import org.cmas.json.EntityEditReply;
import org.cmas.json.doc.DocCreateReply;

import java.util.List;

public interface DocumentService {

    void loadUserDocs(Context context);

    List<Document> getByProfileNoRemoteCall(Context context, long userId, String name);

    Pair<DocCreateReply, String> addNewDoc(Activity activity,
                                           Document document
    );

    Pair<EntityEditReply, String> editDoc(
            Context context,
            Document document
    );

    Pair<EntityEditReply, String> deleteDoc(
            Context context,
            long documentId
    );
}
