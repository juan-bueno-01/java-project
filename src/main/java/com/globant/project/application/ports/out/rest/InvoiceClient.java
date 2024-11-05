package com.globant.project.application.ports.out.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.globant.project.domain.dto.InvoiceClientDTO;
import com.globant.project.domain.dto.InvoiceDTO;
import com.globant.project.domain.dto.InvoiceLoginDTO;
import com.globant.project.domain.dto.InvoiceResponse;
import com.globant.project.domain.dto.InvoiceTokenDTO;

/**
 * InvoiceClient
 */
@FeignClient(name = "electronic-invoice-service", url = "${default.invoice.url}")
public interface InvoiceClient {

    @PostMapping("/login")
    public ResponseEntity<InvoiceTokenDTO> login(InvoiceLoginDTO invoiceLoginDTO);

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestHeader("X-API-TOKEN") String invoiceToken,
            @RequestHeader("X-Requested-With") String requestedWith, InvoiceDTO invoiceDTO);

    @PostMapping("/clients")
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestHeader("X-API-TOKEN") String invoiceToken,
            @RequestHeader("X-Requested-With") String requestedWith, InvoiceClientDTO invoiceClientDTO);
}
