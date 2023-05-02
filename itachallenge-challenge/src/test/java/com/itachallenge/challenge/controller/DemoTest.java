package com.itachallenge.challenge.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DemoTest {

    @Autowired
    Demo demo;

    @Test
    void demoTest(){
        String result = demo.getDemo();
        Assertions.assertEquals("demo version",result);
    }
}
