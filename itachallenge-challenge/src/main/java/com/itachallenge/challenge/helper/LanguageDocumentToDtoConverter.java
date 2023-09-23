package com.itachallenge.challenge.helper;

import com.itachallenge.challenge.document.LanguageDocument;
import com.itachallenge.challenge.dto.LanguageDto;
import com.itachallenge.challenge.exception.ConverterException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class LanguageDocumentToDtoConverter {

    public LanguageDto convertDocumentToDto(LanguageDocument document) throws ConverterException {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(document, LanguageDto.class);
    }
    public Flux<LanguageDto> convertDocumentFluxToDtoFlux(Flux<LanguageDocument> documentFlux) {
        return documentFlux.map(this::convertDocumentToDto);
    }

}
