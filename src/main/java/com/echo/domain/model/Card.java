package com.echo.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Card {
    private final String id;
    private final String question;
    private final String answer;
    private final String tag;
    private Category category;
    private LocalDate lastAnswerDate;

    public Card(String id, String question, String answer, String tag, Category category, LocalDate lastAnswerDate) {
        this.id = id;
        this.question = Objects.requireNonNull(question, "Question cannot be null");
        this.answer = Objects.requireNonNull(answer, "Answer cannot be null");
        this.tag = tag;
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.lastAnswerDate = lastAnswerDate;
    }

    public static Card create(String question, String answer, String tag) {
        return new Card(
                UUID.randomUUID().toString(),
                question,
                answer,
                tag,
                Category.first(),
                null
        );
    }

    public void answerCorrectly(LocalDate date) {
        Category nextCategory = this.category.next();
        if (nextCategory != null) {
            this.category = nextCategory;
        }
        this.lastAnswerDate = date;
    }

    public void answerIncorrectly(LocalDate date) {
        this.category = Category.first();
        this.lastAnswerDate = date;
    }

    public boolean isDone() {
        return this.category == Category.SEVENTH && this.lastAnswerDate != null;
    }

    public boolean shouldBeReviewedOn(LocalDate date) {
        if (lastAnswerDate == null) {
            return true;
        }
        long daysSinceLastAnswer = java.time.temporal.ChronoUnit.DAYS.between(lastAnswerDate, date);
        return daysSinceLastAnswer >= category.getFrequencyDays();
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTag() {
        return tag;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getLastAnswerDate() {
        return lastAnswerDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}