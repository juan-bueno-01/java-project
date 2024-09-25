package com.globant.project.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.globant.project.domain.entities.ProductEntity;

import lombok.experimental.UtilityClass;

/**
 * MenuUtils
 */
@UtilityClass
public class MenuUtils {

    public String formatProductMenu(ProductEntity product) {
        return String.format("%s - %s : %s",
                product.getFantasyName(),
                formatProductPrice(calculateProductPrice(product.getPrice())),
                product.getDescription());
    }

    public BigDecimal calculateProductPrice(BigDecimal productPrice) {
        BigDecimal tax = CalculationUtils.calculateTax(productPrice);
        return CalculationUtils.calculateGrandTotal(productPrice, tax);
    }

    public String formatProductPrice(BigDecimal productPrice) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(productPrice);
    }

    public String getHeader() {
        return "Welcome to Grandma's Food";
    }

    public String formatCategory(String category) {
        return category.replace("_", " ") + ":";
    }

    public byte[] formatMenuPdf(Set<List<ProductEntity>> productsByCategory) throws IOException {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                writeHeader(contentStream);
                writeProducts(contentStream, productsByCategory);
                contentStream.endText();
            }
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void writeHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(100, 750);

        contentStream.showText(getHeader());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
    }

    private void writeProducts(PDPageContentStream contentStream, Set<List<ProductEntity>> productsByCategory)
            throws IOException {
        for (List<ProductEntity> products : productsByCategory) {
            writeCategory(contentStream, products);
            writeProductDetails(contentStream, products);
            contentStream.newLine();
        }
    }

    private void writeCategory(PDPageContentStream contentStream, List<ProductEntity> products) throws IOException {
        String category = formatCategory(products.get(0).getCategory().toString());
        contentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 12);
        contentStream.showText(category);
        contentStream.newLine();
    }

    private void writeProductDetails(PDPageContentStream contentStream, List<ProductEntity> products)
            throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (ProductEntity product : products) {
            String[] productMenu = formatProductMenu(product).split(":");
            contentStream.showText(productMenu[0].trim());
            contentStream.newLine();
            contentStream.showText(productMenu[1].trim());
            contentStream.newLine();
        }
    }
}
