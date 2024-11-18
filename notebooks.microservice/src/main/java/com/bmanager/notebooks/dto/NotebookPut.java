package com.bmanager.notebooks.dto;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class NotebookPut {
    public Long parentDirId;
    public String name;
    private String content;

    public NotebookPut(Long parentDirId, String name, String content) {
        setParentDirId(parentDirId);
        setName(name);
        setContent(content);
    }
}
