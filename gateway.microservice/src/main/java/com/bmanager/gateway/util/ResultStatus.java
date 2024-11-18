package com.bmanager.gateway.util;

public enum ResultStatus {
    OK,
    CREATED,
    UPDATED,
    DELETED,
    InternalError,
    WrongData,
    PermissionDenied,
    NotFound;
}