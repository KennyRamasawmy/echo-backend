package com.echo.infrastructure.adapter.in.web;

import com.echo.domain.model.Card;
import com.echo.domain.model.Category;

public class CardDto {

    private String id;
    private String question;
    private String answer;
    private String tag;
    private Category category;

    public CardDto() {
    }

    public CardDto(String id, String question, String answer, String tag, Category category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tag = tag;
        this.category = category;
    }

    public static CardDto fromDomain(Card card) {
        return new CardDto(
                card.getId(),
                card.getQuestion(),
                card.getAnswer(),
                card.getTag(),
                card.getCategory()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
