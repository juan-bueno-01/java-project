package com.globant.project.application.services.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.globant.project.application.ports.in.services.ErrorService;
import com.globant.project.application.ports.out.repositories.ErrorRepository;
import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ErrorServiceIntegrationTest
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ErrorServiceIntegrationTest {

    @Autowired
    private ErrorService errorService;

    @Autowired
    private ErrorRepository errorRepository;

    private ErrorDTO errorDTO;
    private ErrorDTO errorSaved;

    @BeforeEach
    void setup() {
        errorRepository.deleteAll();
        errorDTO = new ErrorDTO("E4004", "Not Found", "The requested resource was not found");
        errorSaved = errorService.createError(errorDTO);
    }

    @Test
    void givenValidError_whenCreateError_thenErrorIsCreated() {
        assert errorSaved.getErrorCode().equals(errorDTO.getErrorCode());
    }

    @Test
    void givenExistingError_whenCreateError_thenConflictExceptionIsThrown() {
        assertThrows(ConflictException.class, () -> errorService.createError(errorDTO));
    }

    @Test
    void givenExistingError_whenUpdateError_thenErrorIsUpdated() {
        errorDTO.setDescription("The requested resource was not found, please try again");
        errorService.updateError(errorDTO.getErrorCode(), errorDTO);
        assert errorService.getError(errorDTO.getErrorCode()).getDescription().equals(errorDTO.getDescription());
    }

    @Test
    void givenNonExistingError_whenUpdateError_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> errorService.updateError("E4005", errorDTO));
    }

    @Test
    void givenExistingError_whenGetError_thenErrorIsReturned() {
        assert errorService.getError(errorDTO.getErrorCode()).getErrorCode().equals(errorDTO.getErrorCode());
    }

    @Test
    void givenNonExistingError_whenGetError_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> errorService.getError("E4005"));
    }

    @Test
    void givenExistingError_whenDeleteError_thenErrorIsDeleted() {
        errorService.deleteError(errorDTO.getErrorCode());
        assertThrows(NotFoundException.class, () -> errorService.getError(errorDTO.getErrorCode()));
    }

    @Test
    void givenNonExistingError_whenDeleteError_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> errorService.deleteError("E4005"));
    }

    @Test
    void givenExistingError_whenGetDescription_thenDescriptionIsReturned() {
        assertEquals(errorService.getErrorDescription("E4004"), errorSaved.getDescription());
    }

    @Test
    void givenNonExistingError_whenGetDescription_thenNotFoundExceptionIsThrown() {
        assertEquals("Unknown error", errorService.getErrorDescription("E4005"));
    }

    @Test
    void givenExistingError_whenGetException_thenExceptionIsReturned() {
        assertEquals(errorService.getErrorException("E4004", ""), errorSaved.getErrorType() + ": ");
    }

}
