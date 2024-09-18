package com.globant.project.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.services.MenuService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MenuController
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class MenuController {

    private final MenuService menuService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid content type"),
    })
    @GetMapping(value = "v2/menu", produces = { "plain/text", "application/pdf" })
    public ResponseEntity<?> getMenu(@RequestHeader("Content-Type") String contentType) {
        log.info("Request to get menu with content type: {}", contentType);
        if (contentType.equals("application/pdf")) {
            try {
                byte[] pdf = menuService.getMenuPdf();
                return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=menu.pdf")
                        .contentType(MediaType.APPLICATION_PDF).body(pdf);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Internal Server Error" + e.getMessage());
            }
        } else if (contentType.equals("plain/text")) {
            String menu = menuService.getMenuPlainText();
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(menu);
        } else {
            return ResponseEntity.badRequest().body("Invalid content type");
        }

    }
}
