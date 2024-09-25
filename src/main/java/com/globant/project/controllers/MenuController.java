package com.globant.project.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.services.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * MenuController
 */
@RestController
@RequestMapping("/api/v2/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Menu operations")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "Get the menu in plain text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu retrieved"),
    })
    @GetMapping(value = "/plain", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMenuPlainText() {
        String menu = menuService.getMenuPlainText();
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(menu);

    }

    @Operation(summary = "Get the menu in PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getMenuPdf() {
        try {
            byte[] pdf = menuService.getMenuPdf();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=menu.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Error generating PDF: %s", e.getMessage()).getBytes());
        }
    }
}
