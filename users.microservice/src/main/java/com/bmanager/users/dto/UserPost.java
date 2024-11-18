package com.bmanager.users.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserPost {
    private String email;
    private String password;
    private String username;
}
