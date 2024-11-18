package com.bmanager.dirs.util;

import org.springframework.http.HttpStatus;

public enum ResultStatus {
    OK,
    CREATED,
    UPDATED,
    DELETED,
    InternalError,
    WrongData,
    PermissionDenied,
    PermissionDeniedToOtherUsers,
    NotFound;

    public HttpStatus toHttpStatus() {
        switch (this) {
            case OK:
            case UPDATED:
            case DELETED:
                return HttpStatus.OK;
            case CREATED:
                return HttpStatus.CREATED;
            case InternalError:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case WrongData:
                return HttpStatus.BAD_REQUEST;
            case PermissionDenied:
            case PermissionDeniedToOtherUsers:
                return HttpStatus.FORBIDDEN;
            case NotFound:
                return HttpStatus.NOT_FOUND;
        }
        return null;
    }
}