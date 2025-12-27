package com.leitner.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldHaveCorrectFrequencies() {
        assertEquals(1, Category.FIRST.getFrequencyDays());
        assertEquals(2, Category.SECOND.getFrequencyDays());
        assertEquals(4, Category.THIRD.getFrequencyDays());
        assertEquals(8, Category.FOURTH.getFrequencyDays());
        assertEquals(16, Category.FIFTH.getFrequencyDays());
        assertEquals(32, Category.SIXTH.getFrequencyDays());
        assertEquals(64, Category.SEVENTH.getFrequencyDays());
    }

    @Test
    void shouldReturnNextCategory() {
        assertEquals(Category.SECOND, Category.FIRST.next());
        assertEquals(Category.THIRD, Category.SECOND.next());
        assertEquals(Category.FOURTH, Category.THIRD.next());
        assertEquals(Category.FIFTH, Category.FOURTH.next());
        assertEquals(Category.SIXTH, Category.FIFTH.next());
        assertEquals(Category.SEVENTH, Category.SIXTH.next());
    }

    @Test
    void shouldReturnNullForSeventhNext() {
        assertNull(Category.SEVENTH.next());
    }

    @Test
    void shouldReturnFirstCategory() {
        assertEquals(Category.FIRST, Category.first());
    }
}