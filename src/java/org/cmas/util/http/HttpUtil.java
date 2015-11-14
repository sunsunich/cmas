package org.cmas.util.http;

import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    
    private HttpUtil() {
    }

    public static String getIP(HttpServletRequest httpServletRequest) {
        String remoteAddress = httpServletRequest.getHeader("X-Real-IP");
        if (remoteAddress == null) {
            remoteAddress = httpServletRequest.getRemoteAddr();
        }
        return remoteAddress;
    }

    public static Map<String, String> getMapFromRequest(HttpServletRequest request, String varName, String separator) {
        String varAndSep = varName + separator;
        @SuppressWarnings({"unchecked"})
        Map<String, List<Object>> parameterMap = request.getParameterMap();
        Map<String, String> properties = new HashMap<String, String>();
        for (Map.Entry<String, List<Object>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(varName)) {
                String propKey = key.substring(
                        key.indexOf(varAndSep), key.lastIndexOf(separator)
                );
                List<Object> values = entry.getValue();
                @Nullable
                String propVal;
                if(values != null && !values.isEmpty()){
                    propVal = String.valueOf(values.get(0));
                }
                else{
                    propVal = null;
                }
                properties.put(propKey, propVal);
            }
        }
        return properties;
    }
}
