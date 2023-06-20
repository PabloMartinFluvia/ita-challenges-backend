package com.itachallenge.challenge.custom_v2.services_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChallengesServiceDefaultTODO implements ChallengesServiceNEW {

    //TODO: implement method
    @Override
    public Mono<PageDtoTODO> findChallengesPage(int offset, int limit) {
        return null;
    }
}
