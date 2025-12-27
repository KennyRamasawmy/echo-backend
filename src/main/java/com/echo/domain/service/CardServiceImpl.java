package com.echo.domain.service;

import com.echo.domain.model.Card;
import com.echo.domain.model.Category;
import com.echo.domain.port.in.CardService;
import com.echo.domain.port.out.CardRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card createCard(String question, String answer, String tag) {
        Card card = Card.create(question, answer, tag);
        return cardRepository.save(card);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> getCardsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return cardRepository.findAll();
        }
        return cardRepository.findByTags(tags);
    }

    @Override
    public List<Card> getCardsForQuizz(LocalDate date) {
        return cardRepository.findAll().stream()
                .filter(card -> !isCardDone(card))
                .filter(card -> card.shouldBeReviewedOn(date))
                .collect(Collectors.toList());
    }

    private boolean isCardDone(Card card) {
        return card.getCategory() == Category.SEVENTH && card.getLastAnswerDate() != null;
    }

    @Override
    public Optional<Card> answerCard(String cardId, boolean isValid) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isEmpty()) {
            return Optional.empty();
        }

        Card card = optionalCard.get();
        LocalDate today = LocalDate.now();

        if (isValid) {
            card.answerCorrectly(today);
        } else {
            card.answerIncorrectly(today);
        }

        return Optional.of(cardRepository.save(card));
    }
}
