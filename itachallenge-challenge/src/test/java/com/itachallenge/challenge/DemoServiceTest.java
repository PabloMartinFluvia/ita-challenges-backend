package com.itachallenge.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itachallenge.challenge.compilers.ApiResponseDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class DemoServiceTest {

    @Autowired
    private DemoService service;

    @ParameterizedTest
    @MethodSource("provideIndexJson")
    void demoOnlineCoderCompilerTest(Integer indexToTest){
        String jsonPath = "json/ResponsesOnlineCoderCompiler.json";
        ApiResponseDto expected = initResponse(jsonPath, ApiResponseDto[].class)[indexToTest];

        Mono<ApiResponseDto> response = service.demoOnlineCoderCompiler(indexToTest);

        StepVerifier.create(response)
                .assertNext(responseDto -> {
                    System.out.println(responseDto.getOutput());
                    assertEquals(expected.getOutput(),responseDto.getOutput());
                })
                .verifyComplete();
    }

    private static Stream<Integer> provideIndexJson(){
        return Stream.of(
                //cada conjunto es para ir realizando peticiones más complejas
                //así me familiarizaba poco a poco con 3 nuevos lenguajes a la vez :)

               0, // print something python3
                1, // print something php
                2, // print something nodejs //TODO: fix '/n' included at the end of the output due console.log
                3, // print something java

                4, // assign var to sentence result + print var python3
                5, // assign var to sentence result + print var php
                6, // assign var to sentence result + print var //TODO: fix '/n' included at the end of the output due console.log
                7, // assign var to sentence result + print var java

                8, // init 2 vars + declare function 2 args + execute function storing return + print return python3
                9, // init 2 vars + declare function 2 args + execute function storing return + print return php
                10, // init 2 vars + declare function 2 args + execute function storing return + print return node js //TODO: fix '/n' included at the end of the output due console.log
                11, // init 2 vars + declare function 2 args + execute function storing return + print return java

                12, // idem, but the 2 vars are inputs/stdin phyton
                13, // idem, but the 2 vars are inputs/stdin php

                //TODO: need help to code (for NodeJs):
                // obtener 2 enteros from stdin + alacenarlos en 2 variables
                // -> ver último ejemplo de node en en el json de las peticiones
                // valores x,y deben venir de inputs stdin

                14 // idem, but the 2 vars are inputs/stdin java
        );
    }

    private <T> T initResponse(String jsonPath, Class<T> targetClass){
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
