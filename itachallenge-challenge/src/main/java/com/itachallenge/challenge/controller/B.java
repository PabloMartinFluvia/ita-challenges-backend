package com.itachallenge.challenge.controller;

import org.springframework.stereotype.Component;

@Component
public class B {

    private String b = "version";

    public B() {
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
