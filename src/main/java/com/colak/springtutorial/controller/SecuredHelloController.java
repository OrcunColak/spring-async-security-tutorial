package com.colak.springtutorial.controller;

import com.colak.springtutorial.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured")

@RequiredArgsConstructor
@Slf4j
public class SecuredHelloController {

    private final AsyncService asyncService;

    // http://localhost:8080/api/v1/secured/hello
    @GetMapping("/hello")
    public String hello() {
        // Asynchronous ordering problem
        // Run asyncMethod and then run virtualThread
        asyncService.asyncMethod()
                .thenRun(asyncService::virtualThread);

        return "Secured Hello!";
    }

}
