package org.cmas.remote;

import android.content.Context;
import org.cmas.entities.divespot.DiveSpot;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 14/11/12
 * Time: 20:35
 */
public interface RemoteDictionaryService extends BaseRemoteService{

    android.util.Pair<List<DiveSpot>, String> getDiveSpots(Context context, long maxDocTypesVersion) throws Exception;
}
