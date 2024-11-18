package com.bmanager.dirs.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DirPut {
    public Long parentDirId;
    public String name;

    public DirPut(Long parentDirId, String name) {
        setParentDirId(parentDirId);
        setName(name);
    }
}
