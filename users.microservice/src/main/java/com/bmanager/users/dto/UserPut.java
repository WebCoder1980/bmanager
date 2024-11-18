package com.bmanager.users.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserPut {
    private String email;
    private String password;
    private String username;

    public UserPut(String email, String password, String username) {
        setEmail(email);
        setPassword(password);
        setUsername(username);
    }
}
