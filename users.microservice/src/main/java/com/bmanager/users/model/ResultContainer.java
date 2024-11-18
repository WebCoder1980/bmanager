package com.bmanager.users.model;

import com.bmanager.users.util.ResultStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
