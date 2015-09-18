package org.visallo.web.clientapi.codegen;

import org.visallo.web.clientapi.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

public abstract class VisalloApiBase {
    private final String basePath;
    private final Edge edge;
    private final Workspace workspace;
    private final Vertex vertex;
    private final Admin admin;
    private final User user;
    private final LongRunningProcess longRunningProcess;
    private final Ontology ontology;

    public VisalloApiBase(String basePath) {
        this.basePath = basePath;
        edge = new Edge((VisalloApi) this);
        workspace = new Workspace((VisalloApi) this);
        vertex = new Vertex((VisalloApi) this);
        admin = new Admin((VisalloApi) this);
        user = new User((VisalloApi) this);
        longRunningProcess = new LongRunningProcess((VisalloApi) this);
        ontology = new Ontology((VisalloApi) this);
    }

    public static void ignoreSslErrors() {
        try {
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
                public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                    return true;
                }
            });

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            throw new VisalloClientApiException("Could not ignore SSL errors", ex);
        }
    }

    public Edge getEdge() {
        return edge;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Admin getAdmin() {
        return admin;
    }

    public User getUser() {
        return user;
    }

    public LongRunningProcess getLongRunningProcess() {
        return longRunningProcess;
    }

    public Ontology getOntology() {
        return ontology;
    }


    public String getBasePath() {
        return basePath;
    }

    public abstract <T> T execute(String httpVerb, String path, List<Parameter> parameters, Class<T> returnType);

    protected String getParametersAsUrlEncodedString(List<Parameter> parameters) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Parameter parameter : parameters) {
            if (!first) {
                result.append("&");
            }
            result.append(parameter.getName());
            result.append("=");
            result.append(parameterValueToUrlEncodedString(parameter.getValue()));
            first = false;
        }
        return result.toString();
    }

    protected String parameterValueToUrlEncodedString(Object value) {
        try {
            return URLEncoder.encode(value.toString(), "utf8");
        } catch (UnsupportedEncodingException ex) {
            throw new VisalloClientApiException("Could not encode string", ex);
        }
    }

    protected <T> T byteArrayToReturnType(byte[] result, Class<T> cls) {
        try {
            String json = new String(result);
            if (String.class.equals(cls)) {
                if (json.startsWith("\"") && json.endsWith("\"") && json.length() > 1) {
                    return (T) json.substring(1, json.length() - 2);
                } else {
                    return (T) json;
                }
            } else {
                return JsonUtil.getJsonMapper().readValue(json, cls);
            }
        } catch (IOException ex) {
            throw new VisalloClientApiException("Could not convert results to " + cls.getName(), ex);
        }
    }

    protected byte[] readAllFromHttpURLConnection(HttpURLConnection connection) throws IOException {
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[10 * 1024];
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    protected abstract String getSessionCookieValue();

    public abstract String getWorkspaceId();

    protected abstract String getCsrfToken();

    protected String getTargetUrl(String path) {
        return getBasePath() + path;
    }

    public void logout() {
        execute("POST", "/logout", null, null);
    }

    public static class Parameter {
        private String name;
        private Object value;

        public Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public static class MultiPartParameter extends Parameter {
        public MultiPartParameter(String name, String fileName, InputStream in) {
            super(name, new Value(fileName, in));
        }

        public static class Value {
            private String fileName;
            private InputStream in;

            public Value(String fileName, InputStream in) {
                this.fileName = fileName;
                this.in = in;
            }

            public String getFileName() {
                return this.fileName;
            }

            public InputStream getInputStream() {
                return this.in;
            }
        }
    }
}