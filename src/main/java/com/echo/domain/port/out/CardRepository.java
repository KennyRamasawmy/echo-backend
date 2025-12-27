package com.echo.domain.port.out;

import com.echo.domain.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);
    Optional<Card> findById(String id);
    List<Card> findAll();
    List<Card> findByTag(String tag);
    List<Card> findByTags(List<String> tags);
}
