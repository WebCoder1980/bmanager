package com.bmanager.gateway.dto;

import com.bmanager.gateway.util.ResultStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
