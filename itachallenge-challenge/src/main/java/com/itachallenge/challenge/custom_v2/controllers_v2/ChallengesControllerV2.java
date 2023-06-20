package com.itachallenge.challenge.custom_v2.controllers_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.custom_v2.services_v2.ChallengesServiceTODO;
import com.itachallenge.challenge.custom_v2.validations_v2.ValidLimitV2;
import com.itachallenge.challenge.custom_v2.validations_v2.ValidOffsetV2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = ChallengesControllerV2.MICRO_SERVER_CUSTOM)
@Validated //for validating request params
public class ChallengesControllerV2 {

    //OBS: remove /custom before copy paste
    public static final String MICRO_SERVER_CUSTOM =  "/itachallenge/api/v1/challenge/custom";

    public static final String CHALLENGES = "/challenges";

    private final ChallengesServiceTODO service;

    //OBS: @Autowired not needed + constructor can be replaced with @RequiredArgsContructor
    public ChallengesControllerV2(ChallengesServiceTODO service) {
        this.service = service;
    }

    @GetMapping(value = CHALLENGES)
    public Mono<PageDtoTODO> findChallengesPage(@RequestParam @ValidOffsetV2 int offset,
                                                @RequestParam @ValidLimitV2 int limit){
        return service.findChallengesPage(offset, limit);
    }
}
