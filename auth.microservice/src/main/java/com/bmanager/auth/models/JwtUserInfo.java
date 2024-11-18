package com.bmanager.auth.models;

import com.bmanager.auth.security.services.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtUserInfo {
    private Long id;

    public JwtUserInfo(UserDetailsImpl userDetailsImpl) {
        setId(userDetailsImpl.getId());
    }
}
