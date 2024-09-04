package com.globant.project.controllers;

import java.util.List;
import java.util.stream.Collectors;

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
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.mappers.ClientMapper;
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
    private final ClientMapper clientMapper;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDto) {
        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);
        ClientEntity clientSaved = clientService.createClient(clientEntity);
        return new ResponseEntity<>(clientMapper.EntityToDto(clientSaved), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientEntity> clients = clientService.getClients();
        return new ResponseEntity<>(
                clients.stream().map(clientMapper::EntityToDto).collect(Collectors.toList()),
                HttpStatus.OK);

    }

    @GetMapping(path = "/{document}")
    public ResponseEntity<ClientDTO> getClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document) {
        ClientEntity clientEntity = clientService.getClient(document);
        return new ResponseEntity<>(clientMapper.EntityToDto(clientEntity), HttpStatus.OK);

    }

    @PutMapping(path = "/{document}")
    public ResponseEntity<HttpStatus> updateClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document,
            @Valid @RequestBody ClientDTO clientDto) {
        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);
        clientService.updateClient(document, clientEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{document}")
    public ResponseEntity<HttpStatus> deleteClient(
            @Pattern(regexp = "[A-Z]+-\\d{6,}") @PathVariable(name = "document") String document) {
        clientService.deleteClient(document);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
