package org.cmas.remote;

import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.android.storage.entities.i18n.ErrorCode;
import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.StringUtil;

import java.util.ArrayList;
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
        Map<String, Object> params = new HashMap<>();
        params.put("maxVersion", String.valueOf(maxVersion));

        Pair<Pair<List<T>, String>, Map<String, String>> result =
                basicGetRequestSend(url, params, returnType.getType());
        return result.getLeft();
    }

    @Override
    public Pair<List<ErrorCode>, String> getErrorCodes() throws Exception {
        Pair<Pair<String, String>, Map<String, String>> result =
                basicGetRequestSend(appProperties.getGetErrorCodesURL(), new HashMap<>(), String.class);
        Pair<String, String> responseNoCookies = result.getLeft();
        String responseBody = responseNoCookies.getLeft();
        if (StringUtil.isTrimmedEmpty(responseBody)) {
            return Pair.of(null, responseNoCookies.getRight());
        }
        String json = responseBody.substring(responseBody.indexOf('{'), responseBody.lastIndexOf('}') + 1);
        Map<String, String> errorMap = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        List<ErrorCode> errorCodes = new ArrayList<>(errorMap.size());
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            errorCodes.add(new ErrorCode(entry.getKey(), entry.getValue()));
        }
        return Pair.of(errorCodes, "");
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
