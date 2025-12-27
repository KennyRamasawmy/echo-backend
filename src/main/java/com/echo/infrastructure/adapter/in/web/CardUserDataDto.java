package com.echo.infrastructure.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public class CardUserDataDto {

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Answer is required")
    private String answer;

    private String tag;

    public CardUserDataDto() {
    }

    public CardUserDataDto(String question, String answer, String tag) {
        this.question = question;
        this.answer = answer;
        this.tag = tag;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
