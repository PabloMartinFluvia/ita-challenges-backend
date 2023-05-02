package com.itachallenge.challenge.compilers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiResponseDto {

    private String cpuTime; //not relevant

    private String memory; //not relevant

    private String output; //what script prints in console

    private Object language; //not relevant, see shchema in test.resources.json
}
