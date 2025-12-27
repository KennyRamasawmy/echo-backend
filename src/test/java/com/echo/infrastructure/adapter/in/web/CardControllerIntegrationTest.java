package com.echo.infrastructure.adapter.in.web;

import com.echo.application.EchoApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EchoApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCard() throws Exception {
        CardUserDataDto request = new CardUserDataDto("What is TDD?", "Test Driven Development", "Development");

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.question").value("What is TDD?"))
                .andExpect(jsonPath("$.answer").value("Test Driven Development"))
                .andExpect(jsonPath("$.tag").value("Development"))
                .andExpect(jsonPath("$.category").value("FIRST"));
    }

    @Test
    void shouldReturnBadRequestWhenQuestionMissing() throws Exception {
        String json = "{\"answer\": \"Answer\"}";

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAnswerMissing() throws Exception {
        String json = "{\"question\": \"Question?\"}";

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllCards() throws Exception {
        createCard("Q1", "A1", "tag1");
        createCard("Q2", "A2", "tag2");

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetCardsByTag() throws Exception {
        createCard("Q1", "A1", "tag1");
        createCard("Q2", "A2", "tag2");
        createCard("Q3", "A3", "tag1");

        mockMvc.perform(get("/cards").param("tags", "tag1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetQuizzCards() throws Exception {
        createCard("Q1", "A1", null);
        createCard("Q2", "A2", null);

        mockMvc.perform(get("/cards/quizz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetQuizzCardsWithDate() throws Exception {
        createCard("Q1", "A1", null);

        mockMvc.perform(get("/cards/quizz").param("date", "2023-11-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldAnswerCardCorrectly() throws Exception {
        String cardId = createCard("Q1", "A1", null);
        AnswerDto answer = new AnswerDto(true);

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAnswerCardIncorrectly() throws Exception {
        String cardId = createCard("Q1", "A1", null);
        AnswerDto answer = new AnswerDto(false);

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForNonExistentCard() throws Exception {
        AnswerDto answer = new AnswerDto(true);

        mockMvc.perform(patch("/cards/{cardId}/answer", "non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenIsValidMissing() throws Exception {
        String cardId = createCard("Q1", "A1", null);

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldMoveCardToNextCategoryAfterCorrectAnswer() throws Exception {
        String cardId = createCard("Q1", "A1", null);
        AnswerDto answer = new AnswerDto(true);

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("SECOND"));
    }

    @Test
    void shouldMoveCardBackToFirstCategoryAfterIncorrectAnswer() throws Exception {
        String cardId = createCard("Q1", "A1", null);

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AnswerDto(true))))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/cards/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AnswerDto(false))))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("FIRST"));
    }

    private String createCard(String question, String answer, String tag) throws Exception {
        CardUserDataDto request = new CardUserDataDto(question, answer, tag);

        MvcResult result = mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        CardDto createdCard = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CardDto.class
        );

        return createdCard.getId();
    }
}
