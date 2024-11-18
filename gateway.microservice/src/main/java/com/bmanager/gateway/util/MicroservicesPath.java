package com.bmanager.gateway.util;

public enum MicroservicesPath {
    AUTH("http://127.0.0.1:36002"),
    USERS("http://127.0.0.1:36003"),
    DIRS("http://127.0.0.1:36004"),
    NOTEBOOKS("http://127.0.0.1:36005");

    private final String path;

    MicroservicesPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getPathForGateway() {
        return path + "/**";
    }
}
