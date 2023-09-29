package com.itachallenge.challenge.helper;

import com.itachallenge.challenge.document.LanguageDocument;
import com.itachallenge.challenge.dto.LanguageDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageDocumentToDtoConverterTest {

    private final GenericDocumentToDtoConverter<LanguageDocument, LanguageDto> mapper = new GenericDocumentToDtoConverter();

    private LanguageDocument languageDocument1;

    private LanguageDocument languageDocument2;

    private LanguageDto languageDto1;

    private LanguageDto languageDto2;


    @BeforeEach
    public void setUp() {

        UUID[] languageID = new UUID[]{UUID.randomUUID(), UUID.randomUUID()};
        String[] languageNames = new String[]{"Java", "Python"};

        languageDocument1 = new LanguageDocument(languageID[0], languageNames[0]);
        languageDocument2 = new LanguageDocument(languageID[1], languageNames[1]);

        languageDto1 = new LanguageDto(languageID[0], languageNames[0]);
        languageDto2 = new LanguageDto(languageID[1], languageNames[1]);

    }

    @Test
    @DisplayName("Conversion from document to dto when the field types and names perfectly match the source")
    void testConvertLanguageDocumentToLanguageDto() {
        // when
        LanguageDocument languageDocumentMocked = languageDocument1;
        LanguageDto resultDto = mapper.convertDocumentToDto(languageDocumentMocked, LanguageDto.class);
        LanguageDto expectedDto = languageDto1;

        // then
        assertEquals(expectedDto.getLanguageId(), resultDto.getLanguageId());
        assertEquals(expectedDto.getLanguageName(), resultDto.getLanguageName());
    }

    @Test
    @DisplayName("Test convertFluxEntityToFluxDto method")
    void testConvertFluxEntityToFluxDto() {
        Flux<LanguageDocument> documentFlux = Flux.just(languageDocument1, languageDocument2);
        Flux<LanguageDto> resultFlux = mapper.convertDocumentFluxToDtoFlux(documentFlux, LanguageDto.class);

        StepVerifier.create(resultFlux)
                .assertNext(languageDto -> {
                    Assertions.assertEquals(languageDto1.getLanguageId(), languageDto.getLanguageId());
                    Assertions.assertEquals(languageDto1.getLanguageName(), languageDto.getLanguageName());
                })
                .assertNext(languageDto -> {
                    Assertions.assertEquals(languageDto2.getLanguageId(), languageDto.getLanguageId());
                    Assertions.assertEquals(languageDto2.getLanguageName(), languageDto.getLanguageName());
                })
                .expectComplete()
                .verify();
    }

}

