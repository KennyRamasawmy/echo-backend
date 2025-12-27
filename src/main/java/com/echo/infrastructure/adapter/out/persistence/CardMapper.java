package com.echo.infrastructure.adapter.out.persistence;

import com.echo.domain.model.Card;

public class CardMapper {

    private CardMapper() {
    }

    public static Card toDomain(CardEntity entity) {
        return new Card(
                entity.getId(),
                entity.getQuestion(),
                entity.getAnswer(),
                entity.getTag(),
                entity.getCategory(),
                entity.getLastAnswerDate()
        );
    }

    public static CardEntity toEntity(Card card) {
        return new CardEntity(
                card.getId(),
                card.getQuestion(),
                card.getAnswer(),
                card.getTag(),
                card.getCategory(),
                card.getLastAnswerDate()
        );
    }
}
