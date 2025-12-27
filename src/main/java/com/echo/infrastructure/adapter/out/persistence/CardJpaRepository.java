package com.echo.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    List<CardEntity> findByTag(String tag);
    List<CardEntity> findByTagIn(List<String> tags);
}
