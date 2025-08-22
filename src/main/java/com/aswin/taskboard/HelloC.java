package com.aswin.taskboard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloC{
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot is working!";
    }
}
