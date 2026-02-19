package com.dauphine.blogger.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("hello-world")
    public String HelloWorld(){
        return "Hello World ! ";
    }

    @GetMapping("hello")
    public String HelloByName(@RequestParam String name){
        return "Hello" + name;
    }
}
