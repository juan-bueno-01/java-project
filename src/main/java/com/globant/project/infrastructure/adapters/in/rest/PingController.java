package com.globant.project.infrastructure.adapters.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.application.ports.in.services.InvoiceService;

import lombok.RequiredArgsConstructor;

/**
 * PingController
 */
@RestController
@RequestMapping("/ping")
@RequiredArgsConstructor
public class PingController {

    private final InvoiceService invoiceService;

    @GetMapping
    public String ping() {
        invoiceService.getInvoiceServiceToken();
        return "pong";
    }

}
