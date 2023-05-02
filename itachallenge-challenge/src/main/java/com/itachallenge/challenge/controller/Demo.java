package com.itachallenge.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Demo {

    private final String demo = "demo";

    public Demo() {
    }

    public String getDemo(){
        return demo;
    }
}
