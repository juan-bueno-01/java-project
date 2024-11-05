package com.globant.project.application.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.globant.project.application.ports.in.services.InvoiceService;
import com.globant.project.application.ports.out.rest.InvoiceClient;
import com.globant.project.domain.dto.InvoiceClientDTO;
import com.globant.project.domain.dto.InvoiceDTO;
import com.globant.project.domain.dto.InvoiceLoginDTO;
import com.globant.project.domain.dto.InvoiceResponse;
import com.globant.project.domain.dto.InvoiceTokenDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * InvoiceServiceFeignClientImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceFeignClientImpl implements InvoiceService {

    private final InvoiceClient invoiceClient;

    @Value("${default.invoice.email}")
    private String email;

    @Value("${default.invoice.password}")
    private String password;

    @Cacheable("invoiceToken")
    @Scheduled(fixedRate = 600000)
    @Override
    public String getInvoiceServiceToken() {

        InvoiceLoginDTO loginDTO = InvoiceLoginDTO.builder().email(email).password(password).build();

        ResponseEntity<InvoiceTokenDTO> response = invoiceClient.login(loginDTO);

        String invoiceToken = response.getBody().getData().getFirst().getToken().getToken();

        log.info("Invoice token refreshed");

        return invoiceToken;
    }

    @Override
    public String createInvoiceClient(InvoiceClientDTO clientInvoiceDTO) {
        String invoiceToken = getInvoiceServiceToken();

        ResponseEntity<InvoiceResponse> response = invoiceClient.createInvoice(invoiceToken, "XMLHttpRequest",
                clientInvoiceDTO);
        String invoiceClientId = response.getBody().getData().getId();

        log.info("Client registered in invoice system with id: {}", invoiceClientId);

        return invoiceClientId;
    }

    @Override
    public String createInvoice(InvoiceDTO invoiceDTO) {
        String invoiceToken = getInvoiceServiceToken();

        ResponseEntity<InvoiceResponse> response = invoiceClient.createInvoice(invoiceToken, "XMLHttpRequest",
                invoiceDTO);

        String invoiceId = response.getBody().getData().getId();

        log.info("Invoice created in invoice system with id: {}", invoiceId);

        return invoiceId;
    }

}
