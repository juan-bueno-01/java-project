package com.globant.project.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.services.ClientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * ClientController
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDto) {
        ClientDTO clientSaved = clientService.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientSaved);
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping(path = "/{document}")
    public ResponseEntity<ClientDTO> getClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document) {
        ClientDTO clientDto = clientService.getClient(document);
        return ResponseEntity.ok(clientDto);
    }

    @PutMapping(path = "/{document}")
    public ResponseEntity<HttpStatus> updateClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document,
            @Valid @RequestBody ClientDTO clientDto) {
        clientService.updateClient(document, clientDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{document}")
    public ResponseEntity<HttpStatus> deleteClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document) {
        clientService.deleteClient(document);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
