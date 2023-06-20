package com.itachallenge.challenge.custom_v2.services_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import reactor.core.publisher.Mono;

public interface ChallengesServiceV2 {
    Mono<PageDtoTODO> findChallengesPage(int offset, int limit);
}
