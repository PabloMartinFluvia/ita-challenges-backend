package com.itachallenge.challenge.custom_v2.services_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.proxy.HttpProxy;
import com.itachallenge.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChallengesServiceDefaultTODO implements ChallengesServiceNEW {

    private final ChallengeRepository challengeRepo;

    private final HttpProxy proxy;

    //TODO
    @Override
    public Mono<PageDtoTODO> findChallengesPage(int offset, int limit) {
        Mono<PageDtoTODO> pageMono = null;

        //1) Load wanted data (in a single flux)
        //pageMono = challengeRepo.findAll() //Challenge document does not support default sorting
        //        .flatMap(challenge -> proxy.getRequestData());

        //2) Map flux to type that supports any type of sorting criteria

        //3) Apply the page settings (sorting, offset, limit)

        //4) Store the result into a page dto



        return pageMono;
    }
}
