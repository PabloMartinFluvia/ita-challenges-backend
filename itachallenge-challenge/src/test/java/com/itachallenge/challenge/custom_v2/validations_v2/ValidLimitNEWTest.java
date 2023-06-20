package com.itachallenge.challenge.custom_v2.validations_v2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:validationMessages-test.properties")
class ValidLimitNEWTest {

    @Autowired
    private Validator validator;

    @Autowired
    private Environment environment;

    private String limitErrorMsg;

    @BeforeEach
    void init(){
        limitErrorMsg =environment.getProperty("limit.error");
    }

    @ParameterizedTest
    @DisplayName("Valid limit test")
    @MethodSource("validLimit")
    void validLimitTest(int limit){
        ModelWithLimitTest subject = new ModelWithLimitTest(limit);
        Set<ConstraintViolation<ModelWithLimitTest>> violations = validator.validate(subject);
        assertThat(violations).isEmpty();
    }

    static Stream<Integer> validLimit(){
        return Stream.of(1,2);
    }

    @ParameterizedTest
    @DisplayName("Invalid limit test")
    @MethodSource("invalidLimit")
    void invalidLimitTest(int limit){
        ModelWithLimitTest subject = new ModelWithLimitTest(limit);
        Set<ConstraintViolation<ModelWithLimitTest>> violations = validator.validate(subject);
        assertThat(violations).hasSize(1);
        ConstraintViolation<ModelWithLimitTest> violation = violations.stream().toList().get(0);
        assertThat(violation.getMessage()).isEqualTo( limitErrorMsg);
    }

    static Stream<Integer> invalidLimit(){
        return Stream.of(0,-1);
    }

    //inner classes for testing purposes

    @AllArgsConstructor
    class ModelWithLimitTest {

        @ValidLimitNEW
        private int limit;
    }
}
