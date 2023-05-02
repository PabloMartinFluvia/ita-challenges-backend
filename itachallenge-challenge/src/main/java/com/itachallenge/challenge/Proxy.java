package com.itachallenge.challenge;

import com.itachallenge.challenge.compilers.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
public class Proxy {

    private final ApiSpecsProvider specsProvider;
    private WebClient client;

    public Proxy(ApiSpecsProvider workingApisSpec) {
        specsProvider = workingApisSpec;

        client = WebClient.create(); //TODO: configure + optimize
    }

    public Mono<ApiResponseDto> requestCodeExecution(ApiTarget apiTarget, ApiRequestDto apiRequestDto){
        //obbjetivo: que el proxy funcione para cualquier api target
        ApiSpec apiSpec = specsProvider.getApiSpec(apiTarget);
        WebClient.RequestHeadersSpec<?> headersSpec = client
                .method(apiSpec.getRequestMethod())
                .uri(apiSpec.getRequestUrl())
                .contentType(apiSpec.getRequestContentType())
                .bodyValue(apiRequestDto);
        return initHeaders(headersSpec,apiSpec)
                .retrieve()
                .bodyToMono(ApiResponseDto.class);
    }

    private WebClient.RequestHeadersSpec<?> initHeaders(
            WebClient.RequestHeadersSpec<?> headersSpec,
            ApiSpec apiSpec){

        Map<String,String> apiHeadersMap =apiSpec.getCommonHeaders();
        Set<String> requiredApiHeaders = apiHeadersMap.keySet();
        for (String requiredHeader : requiredApiHeaders){
            String valueOfRequierdHeader = apiHeadersMap.get(requiredHeader);
            headersSpec = headersSpec
                    .header(requiredHeader,valueOfRequierdHeader);
        }
        return headersSpec;
    }
}
