package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;
import org.visallo.web.clientapi.util.*;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

public abstract class LongRunningProcessBase extends CategoryBase {
    public LongRunningProcessBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     * @param longRunningProcessId REQUIRED
     */
    public JSONObject get(
        @Required(name = "longRunningProcessId") String longRunningProcessId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("longRunningProcessId", longRunningProcessId));
        return getVisalloApi().execute("GET", "/long-running-process", parameters, JSONObject.class);
    }

    /**
     * @param longRunningProcessId REQUIRED
     */
    public void postCancel(
        @Required(name = "longRunningProcessId") String longRunningProcessId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("longRunningProcessId", longRunningProcessId));
        getVisalloApi().execute("POST", "/long-running-process/cancel", parameters, null);
    }

    /**
     * @param longRunningProcessId REQUIRED
     */
    public void delete(
        @Required(name = "longRunningProcessId") String longRunningProcessId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("longRunningProcessId", longRunningProcessId));
        getVisalloApi().execute("DELETE", "/long-running-process", parameters, null);
    }

}