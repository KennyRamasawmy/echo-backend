package com.echo.domain.service;

import com.echo.domain.model.Card;
import com.echo.domain.model.Category;
import com.echo.domain.port.out.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceImplTest {

    private CardServiceImpl cardService;
    private InMemoryCardRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCardRepository();
        cardService = new CardServiceImpl(repository);
    }

    @Test
    void shouldCreateCard() {
        Card card = cardService.createCard("Queston?", "Answer", "tag1");

        assertNotNull(card.getId());
        assertEquals("Question?", card.getQuestion());
        assertEquals("Answer", card.getAnswer());
        assertEquals("tag1", card.getTag());
        assertEquals(Category.FIRST, card.getCategory());
    }

    @Test
    void shouldGetAllCards() {
        cardService.createCard("Q1", "A1", "tag1");
        cardService.createCard("Q2", "A2", "tag2");

        List<Card> cards = cardService.getAllCards();

        assertEquals(2, cards.size());
    }

    @Test
    void shouldGetCardsByTags() {
        cardService.createCard("Q1", "A1", "tag1");
        cardService.createCard("Q2", "A2", "tag2");
        cardService.createCard("Q3", "A3", "tag1");

        List<Card> cards = cardService.getCardsByTags(List.of("tag1"));

        assertEquals(2, cards.size());
    }

    @Test
    void shouldGetAllCardsWhenTagsIsEmpty() {
        cardService.createCard("Q1", "A1", "tag1");
        cardService.createCard("Q2", "A2", "tag2");

        List<Card> cards = cardService.getCardsByTags(Collections.emptyList());

        assertEquals(2, cards.size());
    }

    @Test
    void shouldGetCardsForQuizz() {
        cardService.createCard("Q1", "A1", null);
        cardService.createCard("Q2", "A2", null);

        List<Card> cards = cardService.getCardsForQuizz(LocalDate.now());

        assertEquals(2, cards.size());
    }

    @Test
    void shouldNotIncludeCardNotDueForReview() {
        Card card = cardService.createCard("Q1", "A1", null);
        cardService.answerCard(card.getId(), true);

        List<Card> cards = cardService.getCardsForQuizz(LocalDate.now());

        assertEquals(0, cards.size());
    }

    @Test
    void shouldAnswerCardCorrectly() {
        Card card = cardService.createCard("Q1", "A1", null);

        Optional<Card> result = cardService.answerCard(card.getId(), true);

        assertTrue(result.isPresent());
        assertEquals(Category.SECOND, result.get().getCategory());
    }

    @Test
    void shouldAnswerCardIncorrectly() {
        Card card = cardService.createCard("Q1", "A1", null);
        cardService.answerCard(card.getId(), true);

        Optional<Card> result = cardService.answerCard(card.getId(), false);

        assertTrue(result.isPresent());
        assertEquals(Category.FIRST, result.get().getCategory());
    }

    @Test
    void shouldReturnEmptyWhenCardNotFound() {
        Optional<Card> result = cardService.answerCard("non-existent-id", true);

        assertTrue(result.isEmpty());
    }

    private static class InMemoryCardRepository implements CardRepository {
        private final Map<String, Card> cards = new HashMap<>();

        @Override
        public Card save(Card card) {
            cards.put(card.getId(), card);
            return card;
        }

        @Override
        public Optional<Card> findById(String id) {
            return Optional.ofNullable(cards.get(id));
        }

        @Override
        public List<Card> findAll() {
            return new ArrayList<>(cards.values());
        }

        @Override
        public List<Card> findByTag(String tag) {
            return cards.values().stream()
                    .filter(c -> tag.equals(c.getTag()))
                    .toList();
        }

        @Override
        public List<Card> findByTags(List<String> tags) {
            return cards.values().stream()
                    .filter(c -> c.getTag() != null && tags.contains(c.getTag()))
                    .toList();
        }
    }
}
