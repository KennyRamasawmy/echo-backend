package com.echo.infrastructure.adapter.out.persistence;

import com.echo.domain.model.Card;
import com.echo.domain.port.out.CardRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CardRepositoryAdapter implements CardRepository {

    private final CardJpaRepository jpaRepository;

    public CardRepositoryAdapter(CardJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Card save(Card card) {
        CardEntity entity = CardMapper.toEntity(card);
        CardEntity savedEntity = jpaRepository.save(entity);
        return CardMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Card> findById(String id) {
        return jpaRepository.findById(id).map(CardMapper::toDomain);
    }

    @Override
    public List<Card> findAll() {
        return jpaRepository.findAll().stream()
                .map(CardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByTag(String tag) {
        return jpaRepository.findByTag(tag).stream()
                .map(CardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByTags(List<String> tags) {
        return jpaRepository.findByTagIn(tags).stream()
                .map(CardMapper::toDomain)
                .collect(Collectors.toList());
    }
}
