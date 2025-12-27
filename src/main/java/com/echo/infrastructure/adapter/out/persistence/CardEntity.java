package com.echo.infrastructure.adapter.out.persistence;

import com.echo.domain.model.Category;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    private String tag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private LocalDate lastAnswerDate;

    public CardEntity() {
    }

    public CardEntity(String id, String question, String answer, String tag, Category category, LocalDate lastAnswerDate) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tag = tag;
        this.category = category;
        this.lastAnswerDate = lastAnswerDate;
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

    public LocalDate getLastAnswerDate() {
        return lastAnswerDate;
    }

    public void setLastAnswerDate(LocalDate lastAnswerDate) {
        this.lastAnswerDate = lastAnswerDate;
    }
}
