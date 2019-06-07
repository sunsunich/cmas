package org.cmas.presentation.service.loyalty.bf;

import com.google.firebase.database.utilities.Pair;
import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.service.loyalty.InsuranceCompanyRemoteService;
import org.cmas.util.http.SimpleHttpsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class BalticFinanceRemoteServiceImpl implements InsuranceCompanyRemoteService {

    private static final Logger LOG = LoggerFactory.getLogger(BalticFinanceRemoteServiceImpl.class);
    public static final Gson BF_GSON = new GsonBuilder().create();

    private String url;
    private String user;
    private String password;


    @Override
    public BalticFinanceResponse sendInsuranceRequest(InsuranceRequest request) throws Exception {
        String postBody = BF_GSON.toJson(new BalticFinanceInsuranceRequest(request));
        LOG.error("sendInsuranceRequest post body: " + postBody);
        Pair<String, Map<String, String>> result = SimpleHttpsClient.sendPostRequest(
                url,
                postBody,
                user,
                password);

        String rawJson = result.getFirst();
        LOG.info(rawJson);
        BalticFinanceResponse response;
        try {
            response = BF_GSON.fromJson(rawJson, BalticFinanceResponse.class);
            response.success = response.bfId != null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response = new BalticFinanceResponse(false, rawJson);
        }
        return response;
    }

    @Required
    public void setUrl(String url) {
        this.url = url;
    }

    @Required
    public void setUser(String user) {
        this.user = user;
    }

    @Required
    public void setPassword(String password) {
        this.password = password;
    }
}
