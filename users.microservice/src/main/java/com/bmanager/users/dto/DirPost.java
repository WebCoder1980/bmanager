package com.bmanager.users.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DirPost {
    public Long parentDirId;
    public String name;
    public Long userId;
}
