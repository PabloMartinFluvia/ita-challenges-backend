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
class ValidOffsetV2Test {

    @Autowired
    private Validator validator;

    @Autowired
    private Environment environment;

    private String offsetErrorMsg;

    @BeforeEach
    void init(){
        offsetErrorMsg = environment.getProperty("offset.error");
    }

    @ParameterizedTest
    @DisplayName("Valid offset test")
    @MethodSource("validOffset")
    void validOffsetTest(int offset){
        ModelWithOffsetTest subject = new ModelWithOffsetTest(offset);
        Set<ConstraintViolation<ModelWithOffsetTest>> violations = validator.validate(subject);
        assertThat(violations).isEmpty();
    }

    static Stream<Integer> validOffset(){
        return Stream.of(0,1);
    }

    @ParameterizedTest
    @DisplayName("Invalid offset test")
    @MethodSource("invalidOffset")
    void invalidOffsetTest(int offset){
        ModelWithOffsetTest subject = new ModelWithOffsetTest(offset);
        Set<ConstraintViolation<ModelWithOffsetTest>> violations = validator.validate(subject);
        assertThat(violations).hasSize(1);
        ConstraintViolation<ModelWithOffsetTest> violation = violations.stream().toList().get(0);
        assertThat(violation.getMessage()).isEqualTo( offsetErrorMsg);
    }

    static Stream<Integer> invalidOffset(){
        return Stream.of(-1,-2);
    }

    //inner classes for testing purposes
    @AllArgsConstructor
    class ModelWithOffsetTest {

        @ValidOffsetV2
        private int offset;
    }
}
