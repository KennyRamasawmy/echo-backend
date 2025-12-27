package com.echo.infrastructure.adapter.in.web;

import jakarta.validation.constraints.NotNull;

public class AnswerDto {

    @NotNull(message = "isValid is required")
    private Boolean isValid;

    public AnswerDto() {
    }

    public AnswerDto(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
}
