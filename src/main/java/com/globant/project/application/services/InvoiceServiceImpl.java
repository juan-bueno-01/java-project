package com.globant.project.application.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.globant.project.application.ports.in.services.InvoiceService;
import com.globant.project.domain.dto.InvoiceClientDTO;
import com.globant.project.domain.dto.InvoiceDTO;
import com.globant.project.domain.dto.InvoiceLoginDTO;
import com.globant.project.domain.dto.InvoiceResponse;
import com.globant.project.domain.dto.InvoiceTokenDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * InvoiceServiceImpl
 */
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final RestClient restClient;

    @Value("${default.invoice.email}")
    private String email;

    @Value("${default.invoice.password}")
    private String password;

    @Value("${default.invoice.url}")
    private String url;

    public InvoiceServiceImpl() {
        this.restClient = RestClient.create();
    }

    @Override
    public String createInvoiceClient(InvoiceClientDTO invoiceClientDTO) {

        String invoiceToken = getInvoiceServiceToken();

        ResponseEntity<InvoiceResponse> response = restClient.post()
                .uri(url + "/clients")
                .headers(httpHeaders -> {
                    httpHeaders.set("X-API-TOKEN", invoiceToken);
                    httpHeaders.set("X-Requested-With", "XMLHttpRequest");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(invoiceClientDTO)
                .retrieve()
                .toEntity(InvoiceResponse.class);

        String invoiceClientId = response.getBody().getData().getId();

        log.info("Client registered in invoice system with id: {}", invoiceClientId);

        return invoiceClientId;

    }

    @Override
    public String createInvoice(InvoiceDTO invoiceDTO) {
        String invoiceToken = getInvoiceServiceToken();

        ResponseEntity<InvoiceResponse> response = restClient.post()
                .uri(url + "/invoices")
                .headers(httpHeaders -> {
                    httpHeaders.set("X-API-TOKEN", invoiceToken);
                    httpHeaders.set("X-Requested-With", "XMLHttpRequest");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(invoiceDTO)
                .retrieve()
                .toEntity(InvoiceResponse.class);

        String invoiceId = response.getBody().getData().getId();
        log.info("Created invoice with ID: {}", invoiceId);

        return invoiceId;

    }

    @Cacheable("invoiceServiceToken")
    @Scheduled(fixedRate = 600000)
    @Override
    public String getInvoiceServiceToken() {

        InvoiceLoginDTO loginDTO = InvoiceLoginDTO.builder().email(email).password(password).build();

        ResponseEntity<InvoiceTokenDTO> response = restClient.post()
                .uri(url + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginDTO)
                .retrieve()
                .toEntity(InvoiceTokenDTO.class);

        String token = response.getBody().getData().getFirst().getToken().getToken();

        log.info("Invoice token refreshed.");

        return token;
    }

}
