package org.cmas.remote;

import org.apache.commons.lang3.tuple.Pair;
import org.cmas.entities.Country;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 14/11/12
 * Time: 20:35
 */
public interface RemoteDictionaryService extends BaseRemoteService {

//    Pair<List<DiveSpot>, String> getDiveSpots(long maxVersion) throws Exception;

    Pair<List<Country>, String> getCountries(long maxVersion) throws Exception;
}
