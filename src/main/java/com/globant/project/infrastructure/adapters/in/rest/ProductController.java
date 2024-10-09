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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.application.ports.in.services.ProductService;
import com.globant.project.application.utils.RegexUtils;
import com.globant.project.domain.dto.ProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * ProductControllers
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product operations")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "409", description = "Product with fantasy name already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid or incomplete product data"),
    })
    @PostMapping("v1/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        ProductDTO productSaved = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product list"),
    })
    @GetMapping("v1/products")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get all products by fantasy name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product list"),
    })
    @GetMapping("v2/products")
    public ResponseEntity<List<ProductDTO>> getProductsByFantasyName(@RequestParam("fantasyName") String fantasyName) {
        List<ProductDTO> products = productService.getProductsByFantasyName(fantasyName);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get a product by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Product UUID is not valid"),
    })
    @GetMapping("v1/products/{uuid}")
    public ResponseEntity<ProductDTO> getProduct(
            @Pattern(regexp = RegexUtils.UUID_REGEX) @PathVariable(name = "uuid") String uuid) {
        ProductDTO product = productService.getProduct(uuid);
        return ResponseEntity.ok(product);

    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "No different field in the request for product"),
            @ApiResponse(responseCode = "409", description = "Product with fantasy name already exists"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "400", description = "Product UUID is not valid"),
    })
    @PutMapping("v1/products/{uuid}")
    public ResponseEntity<Void> updateProduct(
            @Pattern(regexp = RegexUtils.UUID_REGEX) @PathVariable(name = "uuid") String uuid,
            @Valid @RequestBody ProductDTO productDto) {
        productService.updateProduct(uuid, productDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a product by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Product UUID is not valid"),
    })
    @DeleteMapping("v1/products/{uuid}")
    public ResponseEntity<Void> deleteProduct(
            @Pattern(regexp = RegexUtils.UUID_REGEX) @PathVariable(name = "uuid") String uuid) {
        productService.deleteProduct(uuid);
        return ResponseEntity.noContent().build();
    }

}
