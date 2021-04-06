package org.cmas.remote;

import com.google.myjson.reflect.TypeToken;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.sport.NationalFederation;

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
            long maxVersion
            , String url
            , TypeToken<List<T>> returnType
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxVersion", String.valueOf(maxVersion));

        Pair<Pair<List<T>, String>, Map<String, String>> result =
                basicGetRequestSend(url, params, returnType.getType());
        return result.getLeft();
    }

//    @Override
//    public Pair<List<DiveSpot>, String> getDiveSpots(
//            long maxVersion
//    ) throws Exception {
//        return getDictionaryEntities(
//                context,
//                maxVersion,
//                appProperties.getGetDiveSpotsURL(),
//                new TypeToken<List<DiveSpot>>() {}
//        );
//    }

    @Override
    public Pair<List<Country>, String> getCountries(long maxVersion) throws Exception {
        return getDictionaryEntities(
                maxVersion,
                appProperties.getGetCountriesURL(),
                new TypeToken<List<Country>>() {
                }
        );
    }

    @Override
    public Pair<List<NationalFederation>, String> getNationalFederations(long maxVersion) throws Exception {
        return getDictionaryEntities(
                maxVersion,
                appProperties.getGetFederationsURL(),
                new TypeToken<List<NationalFederation>>() {
                }
        );
    }
}
