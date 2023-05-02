package com.itachallenge.challenge.compilers;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiSpecsProvider {
    
    private Map<ApiTarget, ApiSpec> specsProvider;

    @PostConstruct
    private void init(){
        specsProvider = new HashMap<>();
        this.initOnlineCodeCompilerApi();
        //and others...
    }

    //TODO: values no hardcoded
    private void initOnlineCodeCompilerApi() {
        Map<String,String> commonHeaders = new HashMap<>();
        commonHeaders.put("X-RapidAPI-Key","2a497261e9msh06d1774713adaedp177aa0jsnfc89ff1d7af0");
        commonHeaders.put("X-RapidAPI-Host", "online-code-compiler.p.rapidapi.com");
        ApiSpec spec=  ApiSpec.builder()
                .commonHeaders(commonHeaders)
                .requestMethod(HttpMethod.POST)
                .requestUrl("https://online-code-compiler.p.rapidapi.com/v1/")
                .requestContentType(MediaType.APPLICATION_JSON)
                .languagesMethod(HttpMethod.GET)
                .languagesUrl("https://online-code-compiler.p.rapidapi.com/v1/languages/")
                .build();
        specsProvider.put(ApiTarget.ONLINE_CODE_COMPILER, spec);
    }

    public ApiSpec getApiSpec(ApiTarget apiTarget){
        ApiSpec specs = specsProvider.get(apiTarget);
        if(specs != null){
            return specs;
        }else {
            throw new RuntimeException(apiTarget.name() + "has no specifications stored");
        }
    }
}
