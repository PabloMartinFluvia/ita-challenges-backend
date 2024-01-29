package com.itachallenge.user.service;

import com.itachallenge.user.document.SolutionDocument;
import com.itachallenge.user.document.UserSolutionDocument;
import com.itachallenge.user.dtos.SolutionUserDto;
import com.itachallenge.user.dtos.UserScoreDto;
import com.itachallenge.user.dtos.UserSolutionScoreDto;
import com.itachallenge.user.enums.ChallengeStatus;
import com.itachallenge.user.helper.ConverterDocumentToDto;
import com.itachallenge.user.repository.IUserSolutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.UUID;


@Service
public class UserSolutionServiceImp implements IUserSolutionService {

    private final IUserSolutionRepository userSolutionRepository;

    private static final Logger log = LoggerFactory.getLogger(UserSolutionServiceImp.class);

    private final ConverterDocumentToDto converter;

    public UserSolutionServiceImp(IUserSolutionRepository userSolutionRepository, ConverterDocumentToDto converter) {
        this.userSolutionRepository = userSolutionRepository;
        this.converter = converter;
    }

    public Mono<SolutionUserDto<UserScoreDto>> getChallengeById(String idUser, String idChallenge, String idLanguage) {
        UUID userUuid = UUID.fromString(idUser);
        UUID challengeUuid = UUID.fromString(idChallenge);
        UUID languageUuid = UUID.fromString(idLanguage);

        return this.userSolutionRepository.findByUserId(userUuid)
                .filter(userScore -> userScore.getChallengeId().equals(challengeUuid) && userScore.getLanguageId().equals(languageUuid))
                .flatMap(userScore -> converter.fromUserScoreDocumentToUserScoreDto(Flux.just(userScore)))
                .collectList()
                .map(userScoreDtos -> {
                    SolutionUserDto<UserScoreDto> solutionUserDto = new SolutionUserDto<>();
                    solutionUserDto.setInfo(0, 1, 0, userScoreDtos.toArray(new UserScoreDto[0]));
                    return solutionUserDto;
                });
    }

    @Override
    public Mono<UserSolutionScoreDto> addSolution(String idUser,
                                                  String idChallenge,
                                                  String idLanguage,
                                                  String status,
                                                  String solutionText) {
        /*
        TODO - JVR
           Necesario modificar: un usuario puede enviar varias veces la solución para el challenge hasta que esté en estado "ended"
           aunque no podrá enviar varias soluciones para el mismo challenge
       */

        try {
            ChallengeStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            return Mono.error(new IllegalArgumentException("Invalid challenge status: " + status));
        }
        if (status.equalsIgnoreCase("ended")) {
            return Mono.error(new InternalServerErrorException("Internal Server Error Challenge ended!!!"));
        }

        log.info("Valid Status");

        UUID userUuid = UUID.fromString(idUser);
        UUID challengeUuid = UUID.fromString(idChallenge);
        UUID languageUuid = UUID.fromString(idLanguage);

        List<SolutionDocument> solutionDocuments = List.of(
                SolutionDocument.builder()
                        .uuid(UUID.randomUUID())
                        .solutionText(solutionText)
                        .build()
        );

        UserSolutionDocument userSolutionDocument = UserSolutionDocument.builder()
                .uuid(UUID.randomUUID())
                .userId(userUuid)
                .challengeId(challengeUuid)
                .languageId(languageUuid)
                .status(ChallengeStatus.ENDED)
                .score(13)
                .solutionDocument(solutionDocuments)
                .build();

        if (userSolutionDocument.getUuid() == null) {
            userSolutionDocument.setUuid(UUID.randomUUID());
        }

        return userSolutionRepository.save(userSolutionDocument)
                .flatMap(savedDocument -> {
                    UserSolutionScoreDto userSolutionScoreDto = UserSolutionScoreDto.builder()
                            .userId(idUser)
                            .languageId(idLanguage)
                            .challengeId(idChallenge)
                            .solutionText(solutionText)
                            .score(savedDocument.getScore())
                            .build();

                    return Mono.just(userSolutionScoreDto);
                });
    }

    // Custom exception for internal server error
    static class InternalServerErrorException extends RuntimeException {
        public InternalServerErrorException(String message) {
            super(message);
        }
    }
}

