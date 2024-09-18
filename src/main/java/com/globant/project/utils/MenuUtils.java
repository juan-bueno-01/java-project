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

/**
 * MenuUtils
 */
public class MenuUtils {

    public static String formatProductMenu(ProductEntity product) {
        return product.getFantasyName() + " - "
                + formatProductPrice(calculateProductPrice(product.getPrice()))
                + " : "
                + product.getDescription();
    }

    public static BigDecimal calculateProductPrice(BigDecimal productPrice) {
        return CalculationUtils.calculateGrandTotal(productPrice, CalculationUtils.calculateTax(productPrice));
    }

    public static String formatProductPrice(BigDecimal productPrice) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(productPrice);
    }

    public static String getHeader() {
        return "Welcome to Grandma's Food";
    }

    public static String formatCategory(String category) {
        return category.replace("_", " ") + ":";
    }

    public static byte[] formatMenuPdf(Set<List<ProductEntity>> productsByCategory) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(100, 750);

            contentStream.newLine();
            contentStream.showText(MenuUtils.getHeader());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            for (List<ProductEntity> products : productsByCategory) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 12);
                String category = MenuUtils.formatCategory(products.get(0).getCategory().toString());
                contentStream.showText(category);
                contentStream.newLine();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                for (ProductEntity product : products) {
                    String[] productMenu = MenuUtils.formatProductMenu(product).split(":");
                    contentStream.showText(productMenu[0].trim());
                    contentStream.newLine();
                    contentStream.showText(productMenu[1].trim());
                    contentStream.newLine();
                }
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();

            document.save(outputStream);
            return outputStream.toByteArray();
        } finally {
            document.close();
        }

    }

}
