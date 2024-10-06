package com.globant.project.infrastructure.adapters.in.rest;

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

import com.globant.project.application.ports.in.services.ErrorService;
import com.globant.project.application.utils.RegexUtils;
import com.globant.project.domain.dto.ErrorDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * ErrorController
 */
@RestController
@RequestMapping("/api/v2/errors")
@RequiredArgsConstructor
@Tag(name = "Error", description = "Error operations")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class ErrorController {

    private final ErrorService errorService;

    @Operation(summary = "Create a new error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Error created"),
            @ApiResponse(responseCode = "409", description = "Error code already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid or incomplete error data"),
    })
    @PostMapping
    public ResponseEntity<ErrorDTO> createError(@Valid @RequestBody ErrorDTO errorDto) {
        ErrorDTO errorSaved = errorService.createError(errorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(errorSaved);
    }

    @Operation(summary = "Get all errors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Error list"),
    })
    @GetMapping
    public ResponseEntity<List<ErrorDTO>> getAllErrors() {
        List<ErrorDTO> errors = errorService.getErrors();
        return ResponseEntity.status(HttpStatus.OK).body(errors);
    }

    @Operation(summary = "Get an error by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Error found"),
            @ApiResponse(responseCode = "404", description = "Error not found"),
    })
    @GetMapping("/{errorCode}")
    public ResponseEntity<ErrorDTO> getError(
            @Pattern(regexp = RegexUtils.ERROR_CODE_REGEX) @PathVariable(name = "errorCode") String errorCode) {
        ErrorDTO error = errorService.getError(errorCode);
        return ResponseEntity.ok(error);
    }

    @Operation(summary = "Update an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Error updated"),
            @ApiResponse(responseCode = "404", description = "Error not found"),
    })
    @PutMapping("/{errorCode}")
    public ResponseEntity<Void> updateError(
            @Pattern(regexp = RegexUtils.ERROR_CODE_REGEX) @PathVariable(name = "errorCode") String errorCode,
            @Valid @RequestBody ErrorDTO errorDto) {
        errorService.updateError(errorCode, errorDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Error deleted"),
            @ApiResponse(responseCode = "404", description = "Error not found"),
    })
    @DeleteMapping("/{errorCode}")
    public ResponseEntity<Void> deleteError(
            @Pattern(regexp = RegexUtils.ERROR_CODE_REGEX) @PathVariable(name = "errorCode") String errorCode) {
        errorService.deleteError(errorCode);
        return ResponseEntity.noContent().build();
    }

}
