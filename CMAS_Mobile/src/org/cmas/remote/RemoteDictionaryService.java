package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.Country;
import org.cmas.entities.divespot.DiveSpot;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 14/11/12
 * Time: 20:35
 */
public interface RemoteDictionaryService extends BaseRemoteService{

    Pair<List<DiveSpot>, String> getDiveSpots(Context context, long maxVersion) throws Exception;

    Pair<List<Country>, String> getCountries(Context context, long maxVersion) throws Exception;
}
