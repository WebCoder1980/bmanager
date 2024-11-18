package com.bmanager.gateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtDataResponse {
    private UserDataResponse userInfo;
    private Date expiration;
}
