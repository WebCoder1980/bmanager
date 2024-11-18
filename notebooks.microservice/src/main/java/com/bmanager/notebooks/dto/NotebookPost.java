package com.bmanager.notebooks.dto;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class NotebookPost {
    public Long parentDirId;
    public String name;
    public Long userId;

    public NotebookPost(Long parentDirId, String name, Long userId) {
        setParentDirId(parentDirId);
        setName(name);
        setUserId(userId);
    }
}
