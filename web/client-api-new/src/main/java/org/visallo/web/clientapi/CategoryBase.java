package org.visallo.web.clientapi;

public abstract class CategoryBase {
    private final VisalloApi visalloApi;

    protected CategoryBase(VisalloApi visalloApi) {
        this.visalloApi = visalloApi;
    }

    protected VisalloApi getVisalloApi() {
        return visalloApi;
    }
}
