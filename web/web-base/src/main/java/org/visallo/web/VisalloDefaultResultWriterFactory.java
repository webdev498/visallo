package org.visallo.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.v5analytics.webster.HandlerChain;
import com.v5analytics.webster.resultWriters.ResultWriter;
import com.v5analytics.webster.resultWriters.ResultWriterBase;
import com.v5analytics.webster.resultWriters.ResultWriterFactory;
import org.json.JSONObject;
import org.visallo.core.exception.VisalloException;
import org.visallo.web.clientapi.model.ClientApiObject;
import org.visallo.web.clientapi.util.ObjectMapperFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class VisalloDefaultResultWriterFactory implements ResultWriterFactory {
    @Override
    public ResultWriter createResultWriter(Method handleMethod) {
        return new ResultWriterBase(handleMethod) {
            private boolean resultIsClientApiObject;

            @Override
            protected String getContentType(Method handleMethod) {
                if (JSONObject.class.equals(handleMethod.getReturnType())) {
                    return "application/json";
                }
                if (ClientApiObject.class.isAssignableFrom(handleMethod.getReturnType())) {
                    resultIsClientApiObject = true;
                    return "application/json";
                }
                return super.getContentType(handleMethod);
            }

            @Override
            public void write(Object result, HttpServletRequest request, HttpServletResponse response, HandlerChain chain) throws Exception {
                if (result != null) {
                    response.setCharacterEncoding("UTF-8");
                    if (resultIsClientApiObject) {
                        try {
                            String jsonObject = ObjectMapperFactory.getInstance().writeValueAsString(result);
                            response.getWriter().write(jsonObject);
                            return;
                        } catch (JsonProcessingException e) {
                            throw new VisalloException("Could not write json", e);
                        }
                    }
                }
                super.write(result, request, response, chain);
            }
        };
    }
}
