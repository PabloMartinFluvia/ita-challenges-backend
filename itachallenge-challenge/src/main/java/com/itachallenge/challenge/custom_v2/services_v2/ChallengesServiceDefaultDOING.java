package com.itachallenge.challenge.custom_v2.services_v2;

import com.itachallenge.challenge.custom_v2.config_v2.PropertiesConfigTODOUPDATE;
import com.itachallenge.challenge.custom_v2.dtos_v2.ChallengeStatisticsDtoTODO;
import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.custom_v2.helpers_v2.ProvisionalDUMMY;
import com.itachallenge.challenge.custom_v2.repositories_v2.DataBaseTODO;
import com.itachallenge.challenge.documents.Challenge;
import com.itachallenge.challenge.dtos.ChallengeDto;
import com.itachallenge.challenge.proxy.HttpProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengesServiceDefaultDOING implements ChallengesServiceNEW {

    private final DataBaseTODO dataBase;

    private final HttpProxy proxy;

    private final PropertiesConfigTODOUPDATE config;

    //TODO: remove this dummy class when no longer needed
    private final ProvisionalDUMMY provisional;

    /*
    TODO: refactor method. Works but it's a mess / really poor design
    Considerations before refactoring:
    1) 2 main blocks:
        load the data (handling the errors) + map all sources into challenge dto
        sort the dtos + apply pagination settings + create the page dto
        *** sorting may be considered also as a part of pagination settings...
    2) Both blocks will require an update when implementing next user story:
        filters -> update how/which data is loaded
        sorting -> update how dtos are sorted (and how the comparator is obtained)
    3) -> Wait to next user story implementation make sense. Do the refactoring now
    can be a waste of time, specially if in the next user story the design resulting
    of refactoring now does not fit...
    4) But if it's not refactored now, the unit tests are a mess... (see the actual test...)
    5) So... TODO: Proposal: Comment this roadmap to the Scrum Master:
              a) implement a design/structure (refactoring this) that allows sorting criteria + filter criteria
              b) codify user story 1.1 like a "particularity" (filter option: NONE, sorting option : DEFAULT)
              c) do the unit tests for user story 1.1 + PR
              d) extend the logic for the requirements of user story 1.2 + tests + PR

    Some ideas to explore:
        Filtering logic:
            if(X filter){
                logic A;
            } else if (Y filter){
                logic B;
           } etc...
           HORRIBLE

           -> Possible solution: Strategy Pattern
           -> Also: Template Pattern? If the steps order are always the same and only changes few logics...
           -> Alternative more complex: check Chain Of Responsibility + Interpreter? If few reactive steps and order varies...

        Sorting logic:
            Extra service / helper (PageHelper?)
            Has a Map<sorting criteria <Foo>, Comparator<Foo>> (maybe setted?)
            Or maybe one enum for each sortable class, with structure SORTING_OPTION_X(param_value, comparator)?
            Has a method: public PageDto createPage (offset, limit, Flux<T>, Comparator<T)
            ...maybe this service can be refactored later to be more generic...
            ... maybe instead a map can have some object Factory, that provides the Comparator ?

        To avoid too many params in controller and services:
        http://dolszewski.com/spring/how-to-bind-requestparam-to-object/
        more easy to read and validate + simplifies tests
            *And solves the smell code Data Group (offset, limit, sortOption) -> is PACK, always goes together...

        To avoid handle the errors in the service when DB or HttpProxy returns a flux/mono .error:
            -> Proxy pattern (our HttpProxy is too generic, can't handle all future errors (others micros, ita-wiki...)

        For know how to detect a design problem:
        https://www.youtube.com/watch?v=tCnJrcsB_-M&list=PLj2IVmcP-_QOG6580LPjSJhmmau0kfzg-&index=7
        https://www.youtube.com/playlist?list=PLj2IVmcP-_QNVN3PM5a7B6A5lVmQc_Sqg (list)

        To have a global idea of "which pattern can solve 'the design problem that I have'?"
            "I have X problem -> I can apply Y solution/pattern"
            https://www.youtube.com/watch?v=XsmtUAvEl1Q&t=3419s (pt1)
            https://www.youtube.com/watch?v=XsmtUAvEl1Q&t=3275s (pt2)
    */

    @Override
    public Mono<PageDtoTODO> findChallengesPage(int offset, int limit) {

        Mono<PageDtoTODO> pageMono = null;

        //1) Load wanted data (in a single flux)
        pageMono = dataBase.findAllChallenges()
                //TODO: WAITING: add logic for obtain Challenge document correctly populated when data layer is fixed...
                //handle error if DB Fails
                .doOnError(throwable -> true, throwable -> log.error(throwable.getMessage()))
                .onErrorComplete()
                .flatMap(challenge -> proxy.getRequestData(getChallengeStatisticsUrl(challenge.getUuid()), ChallengeStatisticsDtoTODO.class)
                        //handle error if proxy is fails
                        .doOnError(throwable -> true, throwable -> log.error(throwable.getMessage()))
                        .onErrorResume(throwable -> true, throwable -> Mono.just(initDummyStatistics(challenge.getUuid())))
                        // -> challenge doc data + statistics from micro user -> only challenge dto can store both
                        //TODO: PROVISIONAL: depends on how data layer will be fixed
                        .map(statistics -> convertToDto(challenge,statistics)))



        //2) Map flux to type that supports any type of sorting criteria
        // -> not needed: challengeDto supports all sorting criteria

        //3) Apply the page settings (sorting, offset, limit)
                .sort(getChallengesDefaultComparator())
                .skip(offset)
                .take(limit)
                .collectList()
        //4) Store the result into a page dto
                .map(challenges -> createPage(offset, limit, challenges));

        return pageMono;
    }

    private String getChallengeStatisticsUrl(UUID challengeId) {
        String basePath = config.getFindStatisticsURL(); //must match: "..../statistics?challenge="
        return basePath+challengeId;
    }

    private ChallengeStatisticsDtoTODO initDummyStatistics(UUID challengeId) {
        //TODO: replace Provisional logic by the dto constructor
        // return a new challenge statistics dto with dummy/initial values
        return provisional.initDummyStatistics(challengeId, 0, 0);
    }

    //TODO: PROVISIONAL: depends on how data layer will be fixed
    private ChallengeDto convertToDto(Challenge challenge, ChallengeStatisticsDtoTODO statistics) {
        //TODO: replace Provisional logic by Converter logic
        return provisional.convertChallengeDocToDto(challenge, statistics);
    }

    private Comparator<? super ChallengeDto> getChallengesDefaultComparator() {
        return Comparator.comparingInt(ChallengeDto::getPopularity).reversed();
    }

    private PageDtoTODO createPage(int offset, int limit, List<ChallengeDto> challenges) {
        //TODO: replace Provisional logic by  dto constructor
        // return a new PageDto providing this params to constructor
        return provisional.buildPageDto(offset, limit, challenges);
    }
}
