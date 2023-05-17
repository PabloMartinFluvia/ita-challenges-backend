package com.itachallenge.challenge.controller;

import ac.simons.spring.boot.test.autoconfigure.data.mongo.DataMongoTest;
import com.itachallenge.challenge.document.*;
import com.itachallenge.challenge.repository.ChallengeRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.fail;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ChallengeRepositoryTest {

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));



    @DynamicPropertySource
    static void initMongoProperties(@NotNull DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("challengeDB"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());

        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("challengeDB"));
    }

    @Autowired
    private ChallengeRepository challengeRepository;


    UUID uuid_1 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120002");
    UUID uuid_2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");

    /*@Test
    void testDB(){
        Challenge challenge1 = new Challenge(uuid_1, "Loops", Level.EASY, null, new Detail(), null, null, null, null);
        System.out.println("=========");
        System.out.println(challenge1);
        assertNotNull(challengeRepository);


    }*/

    //UUID uuid_1 = UUID.randomUUID();
    //UUID uuid_2 = UUID.randomUUID();


   @BeforeEach
    public void setUp(){
        challengeRepository.deleteAll().block();
        Challenge challenge1 = new Challenge(uuid_1, "Loops", Level.EASY, null, new Detail(), null, null, null, null);
        Challenge challenge2 = new Challenge(uuid_2, "If-Else", Level.EASY, null,new Detail(),null,null,null,null);
        challengeRepository.saveAll(Flux.just(challenge1, challenge2)).blockLast();
    }


   @Test
    void findByUuidTest(){

        Mono<Challenge> firstChallenge = challengeRepository.findByUuid(uuid_1);
        firstChallenge.blockOptional().ifPresentOrElse(
                u -> assertEquals(u.getChallengeId(), uuid_1),
                () -> fail("Challenge not found: " + uuid_1));

        Mono<Challenge> secondChallenge = challengeRepository.findByUuid(uuid_2);
        secondChallenge.blockOptional().ifPresentOrElse(
                u -> assertEquals(u.getChallengeId(), uuid_2),
                () -> fail("Challenge not found: " + uuid_2));
    }

    @Test
    void existsByUuidTest(){
        Challenge exists = challengeRepository.existsByUuid(uuid_1).block();
        assertEquals(exists, true);
    }

    @Test
    void findByNameTest(){

        Mono<Challenge> firstChallenge = challengeRepository.findByName("Loops");
        firstChallenge.blockOptional().ifPresentOrElse(
                u -> assertEquals(u.getName(), "Loops"),
                () -> fail("Challenge with name Loops  not found."));

        Mono<Challenge> secondChallenge = challengeRepository.findByName("If-Else");
        secondChallenge.blockOptional().ifPresentOrElse(
                u -> assertEquals(u.getName(), "If-Else"),
                () -> fail("Challenge with name If-Else not found." ));
    }
}
