package com.globant.project.infrastructure.adapters.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PingController
 */
@RestController
@RequestMapping("/ping")
public class PingController {
    @GetMapping
    public String ping() {
        return "pong";
    }

}
