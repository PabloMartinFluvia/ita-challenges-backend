package com.itachallenge.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Demo {

    private String demo = "demo";

    @Autowired
    private B b;

    public Demo() {
    }

    public String getDemo(){
        return demo+" "+b.getB();
    }
}
