package com.echo.domain.port.in;

import com.echo.domain.model.Card;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardService {
    Card createCard(String question, String answer, String tag);
    List<Card> getAllCards();
    List<Card> getCardsByTags(List<String> tags);
    List<Card> getCardsForQuizz(LocalDate date);
    Optional<Card> answerCard(String cardId, boolean isValid);
}
