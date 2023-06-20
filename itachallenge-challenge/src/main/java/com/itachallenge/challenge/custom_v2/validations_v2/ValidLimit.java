package com.itachallenge.challenge.custom_v2.validations_v2;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Min;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Min(value = 1) //doesn't have sense limit the page size to less than one element
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
//https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-constraint-composition
public @interface ValidLimit {

    String message() default "{limit.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
