package com.itachallenge.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itachallenge.challenge.compilers.ApiTarget;
import com.itachallenge.challenge.compilers.ApiRequestDto;
import com.itachallenge.challenge.compilers.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final Proxy proxy;

    private ApiTarget apiTarget;


    public Mono<ApiResponseDto> demoOnlineCoderCompiler(int indexToTest){
        //index to test: todos los dummys en un mismo json.
        // Sirve para indicar cuales de ellos provar en el test parametrizado

        //Step 1) imaginar que el api target ya estaría seleccionada
        apiTarget = ApiTarget.ONLINE_CODE_COMPILER;
        //TODO: implementar un procedimiento que canbiase el api target cuando esta dejara de funcionar

        //Step 2) método demo que simularía la generación del body de la petición a partir
        //de la info proporcionada por el FE + conociendo la api target
        //La implementación de esta lógica no será poca cosa
        ApiRequestDto requestBody  = demoSetUpRequestBody(indexToTest);

        //Step 3) teniendo la api target seleccionada y un request body coherente con
        //ella -> ya se pude realizar la petición
        return proxy.requestCodeExecution(apiTarget, requestBody);
    }

    private ApiRequestDto demoSetUpRequestBody(int indexToTest){
        String requestsPath = "json/requests";
        String apiFolder = "/online_coder_compiler";
        String bodysJson = "/examples.json";
        String jsonPath = requestsPath+apiFolder+bodysJson;
        return initBody(jsonPath, ApiRequestDto[].class)[indexToTest];
    }

    private <T> T initBody(String jsonPath, Class<T> targetClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = new ClassPathResource(jsonPath).getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            String resourceString = FileCopyUtils.copyToString(reader);
            return mapper.readValue(resourceString, targetClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
