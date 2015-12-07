package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import org.cmas.Globals;
import org.cmas.entities.doc.Document;
import org.cmas.json.EntityEditReply;
import org.cmas.json.doc.DocCreateReply;
import org.cmas.json.doc.DocumentDisplayModel;
import org.cmas.json.doc.EditDocumentSerializer;
import org.cmas.json.doc.NewDocumentSerializer;

import java.util.HashMap;
import java.util.Map;

public class RemoteDocumentServiceImpl extends BaseRemoteServiceImpl implements RemoteDocumentService {

    @Override
    public Pair<DocCreateReply, String> addNewDoc(
            Context context,
            Document document
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Document.class, new NewDocumentSerializer())
                .create();
        params.put("docJson", gson.toJson(document));

        Pair<Pair<DocCreateReply, String>, Map<String, String>> result =
                basicPostRequestSend(
                        context, appProperties.getAddNewDocURL(), params
                        , DocCreateReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<DocumentDisplayModel, String> getUserDocs(Context context, long maxVersion) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxVersion", String.valueOf(maxVersion));

        Pair<Pair<DocumentDisplayModel, String>, Map<String, String>> result =
                basicGetRequestSend(
                        context, appProperties.getGetUserDocsURL(), params
                        , DocumentDisplayModel.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<EntityEditReply, String> editDoc(
            Context context,
            Document document
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Document.class, new EditDocumentSerializer())
                .create();
        params.put("docJson", gson.toJson(document));

        Pair<Pair<EntityEditReply, String>, Map<String, String>> result =
                basicPostRequestSend(
                        context, appProperties.getEditDocURL(), params
                        , EntityEditReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<EntityEditReply, String> deleteDoc(
            Context context,
            final long documentId
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("docId", String.valueOf(documentId));

        Pair<Pair<EntityEditReply, String>, Map<String, String>> result =
                basicGetRequestSend(
                        context, appProperties.getDeleteDocURL(), params
                        , EntityEditReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }
}
