package com.echo.infrastructure.adapter.in.web;

import com.echo.domain.model.Card;
import com.echo.domain.port.in.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards(
            @RequestParam(required = false) String tags) {

        List<String> tagList = parseTagsParameter(tags);
        List<Card> cards = cardService.getCardsByTags(tagList);

        List<CardDto> cardDtos = cards.stream()
                .map(CardDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardDtos);
    }

    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardUserDataDto request) {
        if (request.getQuestion() == null || request.getQuestion().isBlank() ||
                request.getAnswer() == null || request.getAnswer().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Card card = cardService.createCard(
                request.getQuestion(),
                request.getAnswer(),
                request.getTag()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(CardDto.fromDomain(card));
    }

    @GetMapping("/quizz")
    public ResponseEntity<List<CardDto>> getQuizz(
            @RequestParam(required = false) String date) {

        LocalDate quizzDate = parseDate(date);
        List<Card> cards = cardService.getCardsForQuizz(quizzDate);

        List<CardDto> cardDtos = cards.stream()
                .map(CardDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardDtos);
    }

    @PatchMapping("/{cardId}/answer")
    public ResponseEntity<Void> answerCard(
            @PathVariable String cardId,
            @Valid @RequestBody AnswerDto request) {

        if (request.getIsValid() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Card> result = cardService.answerCard(cardId, request.getIsValid());

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private List<String> parseTagsParameter(String tags) {
        if (tags == null || tags.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return LocalDate.now();
        }
    }
}
