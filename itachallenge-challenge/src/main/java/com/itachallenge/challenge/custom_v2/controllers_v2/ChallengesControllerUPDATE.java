package com.itachallenge.challenge.custom_v2.controllers_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.custom_v2.services_v2.ChallengesServiceNEW;
import com.itachallenge.challenge.custom_v2.validations_v2.ValidLimitNEW;
import com.itachallenge.challenge.custom_v2.validations_v2.ValidOffsetNEW;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = ChallengesControllerUPDATE.MICRO_SERVER_CUSTOM)
@Validated //for validating request params
@RequiredArgsConstructor
public class ChallengesControllerUPDATE {

    //OBS: remove /custom before copy and paste
    public static final String MICRO_SERVER_CUSTOM =  "/itachallenge/api/v1/challenge/custom";

    public static final String CHALLENGES = "/challenges";

    private final ChallengesServiceNEW service;


    @GetMapping(value = CHALLENGES)
    public Mono<PageDtoTODO> findChallengesPage(@RequestParam @ValidOffsetNEW int offset,
                                                @RequestParam @ValidLimitNEW int limit){
        return service.findChallengesPage(offset, limit);
    }
}
