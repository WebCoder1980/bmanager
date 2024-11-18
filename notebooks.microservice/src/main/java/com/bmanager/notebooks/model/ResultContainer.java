package com.bmanager.notebooks.model;

import com.bmanager.notebooks.util.ResultStatus;
import lombok.*;

@Getter
@Setter
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ResultContainer<T> {
    private ResultStatus status;
    private T content;

    public ResultContainer(ResultStatus status) {
        setStatus(status);
    }

    public ResultContainer(ResultStatus status, T content) {
        setStatus(status);
        setContent(content);
    }
}
