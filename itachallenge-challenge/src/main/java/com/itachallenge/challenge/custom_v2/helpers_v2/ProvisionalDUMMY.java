package com.itachallenge.challenge.custom_v2.helpers_v2;

import com.itachallenge.challenge.custom_v2.dtos_v2.ChallengeStatisticsDtoTODO;
import com.itachallenge.challenge.custom_v2.dtos_v2.PageDtoTODO;
import com.itachallenge.challenge.documents.Challenge;
import com.itachallenge.challenge.dtos.ChallengeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/*
TODO: delete this dummy class when no longer needed

This class is a "helper" for simulate:
    a) 'new objects creation' in the src code when the target class it's still not implemented.
    b) methods in classes outside the task that we don't know yet how they
     will "look like" (name, params, returned type, if invocations will be done to interfaces,
      if the logic will be implemented calling N classes instead of just one, etc...)

-> Mock the methods on test.
 */
@Component
public class ProvisionalDUMMY {

    //TODO: delete method when ChallengeStatisticsDto is implemented
    public ChallengeStatisticsDtoTODO initDummyStatistics(UUID challengeId, int popularity, float percentage) {
        return null;
    }

    //TODO: delete method when PageDto is implemented
    public PageDtoTODO buildPageDto(int offset, int limit, List<ChallengeDto> challenges) {
        return null;
    }

    //TODO: delete method when Converter is implemented (whe don't know which design will be in develop)
    public ChallengeDto convertChallengeDocToDto(Challenge challenge, ChallengeStatisticsDtoTODO statistics) {
        return null;
    }
}
