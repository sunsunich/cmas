package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 07/12/12
 * Time: 02:51
 */
public class GsonView extends AbstractView implements View {

    private Object toSerialize;
    private Gson gson;

    public GsonView(Object toSerialize, Gson gson) {
        super();
        this.toSerialize = toSerialize;
        this.gson = gson;
        setContentType("text/plain;charset=UTF-8");
    }

    @Override
    protected void renderMergedOutputModel(Map map,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setStatus(HttpURLConnection.HTTP_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(getContentType());

        gson.toJson(toSerialize, response.getWriter());
    }
}
