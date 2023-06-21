package com.itachallenge.challenge.custom.services_v2;

import com.itachallenge.challenge.custom_v2.config_v2.PropertiesConfigTODOUPDATE;
import com.itachallenge.challenge.custom_v2.dtos_v2.ChallengeStatisticsDtoTODO;
import com.itachallenge.challenge.custom_v2.helpers_v2.ProvisionalDUMMY;
import com.itachallenge.challenge.custom_v2.repositories_v2.DataBaseTODO;
import com.itachallenge.challenge.custom_v2.services_v2.ChallengesServiceDefaultDOING;
import com.itachallenge.challenge.documents.*;
import com.itachallenge.challenge.proxy.HttpProxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ChallengesServiceDefaultDOINGTest {

    @Autowired
    private ChallengesServiceDefaultDOING service;

    @MockBean
    private DataBaseTODO dataBase;

    @MockBean
    private HttpProxy proxy;

    @MockBean
    private PropertiesConfigTODOUPDATE config;

    //TODO: remove this dummy class when no longer needed
    @MockBean
    private ProvisionalDUMMY provisional;

    private static final int allSize = 10;

    private static final int statisticsFoundBeforeMicroUserFailure = allSize - 7;

    private static List<UUID> challengesId;

    private static List<ChallengeDocTest> challenges;

    private static List<ChallengeStatisticsDtoTest> statistics;

    @BeforeAll
    static void setUp(){
        challengesId = new ArrayList<>();
        challenges = new ArrayList<>();
        Set<Language> languagesDocs = Set.of(
                new Language(1, "lang1", null),
                new Language(2, "lang2", null),
                new Language(3, "lang3", null));
        statistics = new ArrayList<>();

        for(int i = 1; i<= allSize; i++){
            UUID challengeId = UUID.randomUUID();
            challengesId.add(challengeId);
            challenges.add(new ChallengeDocTest(challengeId, "title"+i, languagesDocs));
            if(i <= statisticsFoundBeforeMicroUserFailure){
                statistics.add(new ChallengeStatisticsDtoTest(challengeId, i, i));
            }
        }
    }

    @Test
    void findChallengesPageWithoutFilteringAndDefaultSortingTest(){

        //TODO: do a unit test for this method it's a caos, due the awful code implemented in service...
        /*
        when(dataBase.findAllChallenges()).thenReturn(Flux.fromIterable(challenges));
        String microUserUrl = "..../statistics?challenge=";
        when(config.getFindStatisticsURL()).thenReturn(microUserUrl);
        for(int i = 0; i < allSize; i++){
            if(i< statisticsFoundBeforeMicroUserFailure){
                when(proxy.getRequestData(microUserUrl+challengesId.get(i), ChallengeStatisticsDtoTODO.class))
                        .thenReturn(Mono.just(statistics.get(i)));
            }else{
                when(proxy.getRequestData(microUserUrl+challengesId.get(i), ChallengeStatisticsDtoTODO.class))
                        .thenThrow(WebClientException.class);
                when(provisional.initDummyStatistics(challengesId.get(i),0,0))
                        .thenReturn(new ChallengeStatisticsDtoTest(challengesId.get(i),0,0));
            }
            //when(provisional.convertChallengeDocToDto())
        }

         */



        //MOCK MOCK MOCK....

    }

    //inner classes for test

    static class ChallengeDocTest extends Challenge{

        //fixes what is needed in document. add @Transient. Fix constructor (and do tests to verify)
        private Set<Language> languagesDocs;

        public ChallengeDocTest(UUID uuid, String title, Set<Language> languagesDocs) {
            //TODO: fix super constructor once fixed Challenge in develop
            super(uuid, null, title, null, null, null, null, null, null);
            this.languagesDocs = languagesDocs;
        }

        public Set<Language> getLanguagesDocs() {
            return languagesDocs;
        }

        public void setLanguagesDocs(Set<Language> languagesDocs) {
            this.languagesDocs = languagesDocs;
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    static class ChallengeStatisticsDtoTest extends ChallengeStatisticsDtoTODO{

        private UUID challengeId;

        private int popularity;

        private float percentage;
    }
}
