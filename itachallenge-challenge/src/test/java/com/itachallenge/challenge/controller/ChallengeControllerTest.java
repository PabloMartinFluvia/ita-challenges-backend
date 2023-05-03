package com.itachallenge.challenge.controller;

import com.itachallenge.challenge.dto.challengessection.FiltersDto;
import com.itachallenge.challenge.dto.challengessection.SortInfoDto;
import com.itachallenge.challenge.helper.ResourceHelper;
import com.itachallenge.challenge.service.ChallengeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ChallengeController.class)
class ChallengeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ChallengeService challengeService;

    private static ResourceHelper resourceHelper;

    @BeforeAll
    static void setUp(){
        resourceHelper = new ResourceHelper();
    }

    @Test
    @DisplayName("GET filters options test")
    void getChallengesSectionOptionsTest(){
        String jsonPath = "json/Filters.json";
        FiltersDto expected =  resourceHelper.mapResource(jsonPath, FiltersDto.class);
        when(challengeService.getChallengesFilters()).thenReturn(Mono.just(expected));

        String uri = ChallengeController.CHALLENGE+ChallengeController.FILTERS;
        webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FiltersDto.class)
                .value(Assertions::assertNotNull)
                .value(result -> assertThat(result).usingRecursiveComparison().isEqualTo(expected));
    }

    @Test
    @DisplayName("GET sorting info test")
    void getChallengesSortInfoTest(){
        String jsonPath = "json/SortInfo.json";
        SortInfoDto expected =  resourceHelper.mapResource(jsonPath, SortInfoDto.class);
        when(challengeService.getChallengesSortInfo()).thenReturn(Mono.just(expected));

        String uri = ChallengeController.CHALLENGE+ChallengeController.SORT;
        webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SortInfoDto.class)
                .value(Assertions::assertNotNull)
                .value(result -> assertThat(result).usingRecursiveComparison().isEqualTo(expected));
    }
}
