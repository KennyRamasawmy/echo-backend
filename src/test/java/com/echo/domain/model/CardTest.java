package com.leitner.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void shouldCreateCardWithCategoryFirst() {
        Card card = Card.create("Question?", "Answer", "tag1");

        assertNotNull(card.getId());
        assertEquals("Question?", card.getQuestion());
        assertEquals("Answer", card.getAnswer());
        assertEquals("tag1", card.getTag());
        assertEquals(Category.FIRST, card.getCategory());
        assertNull(card.getLastAnswerDate());
    }

    @Test
    void shouldMoveToNextCategoryWhenAnsweredCorrectly() {
        Card card = Card.create("Question?", "Answer", null);
        LocalDate today = LocalDate.now();

        card.answerCorrectly(today);

        assertEquals(Category.SECOND, card.getCategory());
        assertEquals(today, card.getLastAnswerDate());
    }

    @Test
    void shouldMoveBackToFirstCategoryWhenAnsweredIncorrectly() {
        Card card = new Card("id", "Q", "A", null, Category.THIRD, null);
        LocalDate today = LocalDate.now();

        card.answerIncorrectly(today);

        assertEquals(Category.FIRST, card.getCategory());
        assertEquals(today, card.getLastAnswerDate());
    }

    @Test
    void shouldBeReviewedWhenNeverAnswered() {
        Card card = Card.create("Q", "A", null);

        assertTrue(card.shouldBeReviewedOn(LocalDate.now()));
    }

    @Test
    void shouldNotBeReviewedBeforeFrequencyDays() {
        LocalDate lastAnswer = LocalDate.now().minusDays(1);
        Card card = new Card("id", "Q", "A", null, Category.SECOND, lastAnswer);

        assertFalse(card.shouldBeReviewedOn(LocalDate.now()));
    }

    @Test
    void shouldBeReviewedAfterFrequencyDays() {
        LocalDate lastAnswer = LocalDate.now().minusDays(2);
        Card card = new Card("id", "Q", "A", null, Category.SECOND, lastAnswer);

        assertTrue(card.shouldBeReviewedOn(LocalDate.now()));
    }

    @Test
    void shouldProgressThroughAllCategories() {
        Card card = Card.create("Q", "A", null);
        LocalDate today = LocalDate.now();

        card.answerCorrectly(today);
        assertEquals(Category.SECOND, card.getCategory());

        card.answerCorrectly(today);
        assertEquals(Category.THIRD, card.getCategory());

        card.answerCorrectly(today);
        assertEquals(Category.FOURTH, card.getCategory());

        card.answerCorrectly(today);
        assertEquals(Category.FIFTH, card.getCategory());

        card.answerCorrectly(today);
        assertEquals(Category.SIXTH, card.getCategory());

        card.answerCorrectly(today);
        assertEquals(Category.SEVENTH, card.getCategory());
    }

    @Test
    void shouldStayAtSeventhWhenAnsweredCorrectly() {
        Card card = new Card("id", "Q", "A", null, Category.SEVENTH, null);
        LocalDate today = LocalDate.now();

        card.answerCorrectly(today);

        assertEquals(Category.SEVENTH, card.getCategory());
    }
}