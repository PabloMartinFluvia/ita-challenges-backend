package com.itachallenge.challenge.compilers;


import lombok.*;

@Getter
@Setter
public class ApiRequestDto {

    private String language; //use lowercase + see resources.json.languages and check used compiler json.

    private String version; // technology Version Index Number or "latest"

    private String code; //see example resource

    private String input; // online code compiler: stdin value (User input)

    public ApiRequestDto() {
        //empty constructor for dummies deserialization
    }
}
