package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import com.google.myjson.reflect.TypeToken;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.doc.DocumentType;
import org.cmas.json.doc.DocDictDataForClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 14/11/12
 * Time: 20:36
 */
public class RemoteDictionaryServiceImpl extends BaseRemoteServiceImpl implements RemoteDictionaryService {


    private <T extends DictionaryEntity> Pair<List<T>, String> getDictionaryEntities(
            Context context
            , long maxVersion
            , String url
            , TypeToken<List<T>> returnType
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxVersion", String.valueOf(maxVersion));

        Pair<Pair<List<T>, String>, Map<String, String>> result =
                basicGetRequestSend(context, url, params, returnType.getType());
        return result.first;
    }

    @Override
    public Pair<List<DocumentType>, String> getDocDict(
            Context context,
            long maxDocTypesVersion
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxDocTypesVersion", String.valueOf(maxDocTypesVersion));

        Pair<Pair<DocDictDataForClient, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getGetDocsDictURL(), params, DocDictDataForClient.class);
        return new Pair<List<DocumentType>, String>(
                result.first.first.getDocTypes(), result.first.second
        );
    }
}
