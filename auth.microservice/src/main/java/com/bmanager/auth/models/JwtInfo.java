package com.bmanager.auth.models;

import java.util.Date;

public class JwtInfo {
    private JwtUserInfo userInfo;
    private Date expiration;

    public JwtUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(JwtUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
