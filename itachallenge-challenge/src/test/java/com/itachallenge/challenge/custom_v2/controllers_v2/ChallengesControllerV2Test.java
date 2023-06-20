package com.itachallenge.challenge.custom_v2.controllers_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.custom_v2.exceptions_v2.ErrorDtoPROVISIONAL;
import com.itachallenge.challenge.custom_v2.services_v2.ChallengesServiceV2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ChallengesControllerV2.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@TestPropertySource("classpath:validationMessages-test.properties")
class ChallengesControllerV2Test {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private Environment environment;

    @MockBean
    private ChallengesServiceV2 service;

    @ParameterizedTest
    @DisplayName("Params limit/offset invalid values test")
    @MethodSource("initInvalidLimitOffset")
    void invalidOffsetLimitTest(String offset, String limit, String[] errorsMsgPropertyKeys){
        String url = ChallengesControllerV2.MICRO_SERVER_CUSTOM+ ChallengesControllerV2.CHALLENGES;
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("offset",offset)
                        .queryParam("limit", limit)
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(ErrorDtoPROVISIONAL.class)
                .consumeWith(result -> {
                            Flux<ErrorDtoPROVISIONAL> errorFlux = result.getResponseBody();
                            Set<String> expectedMessages = expectedErrorsMsgs(errorsMsgPropertyKeys);
                            StepVerifier.create(errorFlux)
                                    .assertNext(errorC -> {
                                        assertNotNull(errorC);
                                        assertEquals(expectedMessages.size(), errorC.getErrorsMsg().size());
                                        assertTrue(errorC.getErrorsMsg().containsAll(expectedMessages));
                                    })
                                    .verifyComplete();
                        }
                );
    }

    private Set<String> expectedErrorsMsgs(String... errorPropertiesKeys){
        Set<String> errorMessages = new HashSet<>();
        for(String errorKey: errorPropertiesKeys){
            errorMessages.add(environment.getProperty(errorKey));
        }
        return errorMessages;
    }

    static Stream<Arguments> initInvalidLimitOffset(){
        return Stream.of(
                Arguments.of("-1","1",new String[]{"offset.error"}), //constraint offset
                Arguments.of("0","0",new String[]{"limit.error"}), //constraint limit
                Arguments.of("-1","0",new String[]{"offset.error", "limit.error"}), //constraint both
                Arguments.of("2.5","2",new String[]{"offset.type"}), //type offset
                Arguments.of("2","b",new String[]{"limit.type"}), //type limit
                Arguments.of("2.5","b",new String[]{"offset.type"}) //both types -> exception to first
        );
    }

    @ParameterizedTest
    @DisplayName("Test: offset or limit not provided")
    @MethodSource("initNotProvided")
    void offsetNotProvidedTest(String notProvidedPropertyKey, String paramProvided, int providedValue){
        String url = ChallengesControllerV2.MICRO_SERVER_CUSTOM+ ChallengesControllerV2.CHALLENGES;
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam(paramProvided, providedValue)
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(ErrorDtoPROVISIONAL.class)
                .consumeWith(result -> {
                            Flux<ErrorDtoPROVISIONAL> errorFlux = result.getResponseBody();
                            Set<String> expectedMessages = Set.of(environment.getProperty(notProvidedPropertyKey));
                            StepVerifier.create(errorFlux)
                                    .assertNext(errorC -> {
                                        assertNotNull(errorC);
                                        assertEquals(expectedMessages.size(), errorC.getErrorsMsg().size());
                                        assertTrue(errorC.getErrorsMsg().containsAll(expectedMessages));
                                    })
                                    .verifyComplete();
                        }
                );
    }

    static Stream<Arguments> initNotProvided(){
        return Stream.of(
                Arguments.of("offset.not-provided", "limit", 100),
                Arguments.of("limit.not-provided", "offset", 100)
        );
    }

    @Test
    @DisplayName("Test: service receives page params and controller returns the page provided")
    void findChallengesPageTest(){
        int offset = 0;
        int limit = 3;
        ItemTest item1 = new ItemTest(UUID.randomUUID(), "title1");
        PageDtoTODO pageTest = new PageTest(offset, limit, List.of(item1, item1, item1));
        when(service.findChallengesPage(offset,limit)).thenReturn(Mono.just(pageTest));

        String url = ChallengesControllerV2.MICRO_SERVER_CUSTOM+ ChallengesControllerV2.CHALLENGES;
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("offset",offset)
                        .queryParam("limit", limit)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .returnResult(PageTest.class)
                .consumeWith(result -> {
                            Flux<PageTest> pageFlux = result.getResponseBody();
                            StepVerifier.create(pageFlux)
                                    .assertNext(page ->
                                            assertThat(page).usingRecursiveComparison().isEqualTo(pageTest))
                                    .verifyComplete();
                        }
                );
    }

    //inner classes for testing purposes

    @AllArgsConstructor
    @Getter
    static class PageTest extends PageDtoTODO {

        private int offset;

        private int limit;

        private List<ItemTest> results;

        public int getCount(){
            return results.size();
        }
    }

    @AllArgsConstructor
    @Getter
    static class ItemTest {

        private UUID testId;

        private String title;
    }
}
