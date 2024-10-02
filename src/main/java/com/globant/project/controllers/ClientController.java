package com.globant.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.services.ClientService;
import com.globant.project.utils.RegexUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * ClientController
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client operations")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "409", description = "Client document already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid or incomplete client data"),
    })
    @PostMapping("v1/clients")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDto) {
        ClientDTO clientSaved = clientService.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientSaved);
    }

    // @Operation(summary = "Get all clients")
    // @ApiResponses(value = {
    // @ApiResponse(responseCode = "200", description = "Client list"),
    // })
    // @GetMapping("v1/clients")
    // public ResponseEntity<List<ClientDTO>> getClients() {
    // List<ClientDTO> clients = clientService.getClients();
    // return ResponseEntity.ok(clients);
    // }

    @Operation(summary = "Get all clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client list"),
    })
    @GetMapping("v2/clients")
    public ResponseEntity<List<ClientDTO>> getClientsOrderedBy(@RequestParam(name = "orderBy") Optional<String> orderBy,
            @RequestParam(name = "direction") Optional<String> direction) {
        List<ClientDTO> clients = clientService.getClients(orderBy, direction);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Client document is not valid"),
    })
    @GetMapping("v1/clients/{document}")
    public ResponseEntity<ClientDTO> getClient(
            @Pattern(regexp = RegexUtils.DOCUMENT_REGEX) @PathVariable(name = "document") String document) {
        ClientDTO clientDto = clientService.getClient(document);
        return ResponseEntity.ok(clientDto);
    }

    @Operation(summary = "Update a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client updated"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Client document is not valid"),
    })
    @PutMapping("v1/clients/{document}")
    public ResponseEntity<Void> updateClient(
            @Pattern(regexp = RegexUtils.DOCUMENT_REGEX) @PathVariable(name = "document") String document,
            @Valid @RequestBody ClientDTO clientDto) {
        clientService.updateClient(document, clientDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Client document is not valid"),
    })
    @DeleteMapping("v1/clients/{document}")
    public ResponseEntity<Void> deleteClient(
            @Pattern(regexp = RegexUtils.DOCUMENT_REGEX) @PathVariable(name = "document") String document) {
        clientService.deleteClient(document);
        return ResponseEntity.noContent().build();
    }
}
