package com.bmanager.dirs.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DirPost {
    public Long parentDirId;
    public String name;

    public DirPost(Long parentDirId, String name) {
        setParentDirId(parentDirId);
        setName(name);
    }
}
