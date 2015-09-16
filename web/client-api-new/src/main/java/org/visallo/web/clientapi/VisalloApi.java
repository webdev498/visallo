package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.VisalloApiBase;

public class VisalloApi extends VisalloApiBase {
    private String sessionCookieValue;
    private String workspaceId;
    private String csrfToken;

    public VisalloApi(String basePath) {
        this(basePath, false);
    }

    public VisalloApi(String basePath, boolean ignoreSslErrors) {
        super(basePath, ignoreSslErrors);
    }

    public void setSessionCookie(String sessionCookieValue) {
        this.sessionCookieValue = sessionCookieValue;
    }

    @Override
    protected String getSessionCookieValue() {
        return sessionCookieValue;
    }

    @Override
    protected String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    @Override
    public String getCsrfToken() {
        return csrfToken;
    }
}