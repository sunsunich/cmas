package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.doc.DocumentType;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 14/11/12
 * Time: 20:35
 */
public interface RemoteDictionaryService extends BaseRemoteService{

    Pair<List<DocumentType>, String> getDocDict(Context context, long maxDocTypesVersion) throws Exception;
}
