package com.globant.project.application.services.unitTests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;

import com.globant.project.application.mappers.ErrorMapper;
import com.globant.project.application.ports.out.repositories.ErrorRepository;
import com.globant.project.application.services.ErrorServiceImpl;
import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.domain.entities.ErrorEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ErrorServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ErrorServiceUnitTest {

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private ErrorMapper errorMapper;

    @InjectMocks
    private ErrorServiceImpl errorService;

    private ErrorDTO errorDto;
    private ErrorEntity errorEntity;

    @BeforeEach
    public void setup() {
        errorDto = new ErrorDTO("E4004", "Not Found", "The requested resource was not found", LocalDateTime.now(),
                LocalDateTime.now());
        errorEntity = new ErrorEntity("E4004", "Not Found", "The requested resource was not found", LocalDateTime.now(),
                LocalDateTime.now());

        lenient().when(errorMapper.DtoToEntity(errorDto)).thenReturn(errorEntity);
        lenient().when(errorMapper.EntityToDto(errorEntity)).thenReturn(errorDto);
    }

    @Test
    public void createError_WhenErrorDoesNotExist_ShouldCreateError() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(false);
        when(errorRepository.save(errorEntity)).thenReturn(errorEntity);

        ErrorDTO result = errorService.createError(errorDto);
        assertEquals(errorDto, result);
    }

    @Test
    public void createError_WhenErrorExists_ShouldThrowConflictException() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(true);

        assertThrows(ConflictException.class, () -> errorService.createError(errorDto));
    }

    @Test
    public void deleteError_WhenErrorExists_ShouldDeleteError() {
        when(errorRepository.findById(errorDto.getErrorCode())).thenReturn(Optional.of(errorEntity));

        errorService.deleteError(errorDto.getErrorCode());

        verify(errorRepository, times(1)).delete(errorEntity);

    }

    @Test
    public void deleteError_WhenErrorDoesNotExist_ShouldThrowConflictException() {
        when(errorRepository.findById(errorDto.getErrorCode())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> errorService.deleteError(errorDto.getErrorCode()));
    }

    @Test
    public void getError_WhenErrorExists_ShouldReturnError() {
        when(errorRepository.findById(errorDto.getErrorCode())).thenReturn(Optional.of(errorEntity));

        ErrorDTO result = errorService.getError(errorDto.getErrorCode());

        assertEquals(errorDto, result);

    }

    @Test
    public void updateError_WhenErrorExists_ShouldUpdateError() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(true);
        when(errorRepository.save(errorEntity)).thenReturn(errorEntity);

        errorService.updateError(errorDto.getErrorCode(), errorDto);

        verify(errorRepository, times(1)).save(errorEntity);
    }

    @Test
    public void updateError_WhenErrorDoesNotExist_ShouldThrowConflictException() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> errorService.updateError(errorDto.getErrorCode(), errorDto));
    }

    @Test
    public void errorExists_WhenErrorExists_ShouldReturnTrue() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(true);

        boolean result = errorService.errorExists(errorDto.getErrorCode());

        assertEquals(true, result);
    }

    @Test
    public void errorExists_WhenErrorDoesNotExist_ShouldReturnFalse() {
        when(errorRepository.existsById(errorDto.getErrorCode())).thenReturn(false);

        boolean result = errorService.errorExists(errorDto.getErrorCode());

        assertEquals(false, result);
    }

    @Test
    public void getErrors_WhenErrorsExist_ShouldReturnErrors() {
        when(errorRepository.findAll()).thenReturn(List.of(errorEntity));

        assertEquals(List.of(errorDto), errorService.getErrors());
    }

    @Test
    public void getErrorDescription_WhenErrorDoesNotExists_ShouldReturnDescription() {
        String result = errorService.getErrorDescription("E4005");
        assertEquals("Unknown error", result);
    }

    @Test
    public void getErrorException_WhenErrorDoesNotExists_ShouldReturnException() {
        String result = errorService.getErrorException("E4005", "");
        assertEquals("Unknown exception", result);
    }

    // @Test
    // public void getErrorException_WhenErrorExists_ShouldReturnException() {
    // errorService.loadErrorCodes();
    // String result = errorService.getErrorException("E4004", "");
    // assertEquals("Not Found: ", result);
    // }

    // @Test
    // public void getErrorDescription_WhenErrorExists_ShouldReturnDescription() {
    // errorService.loadErrorCodes();
    // String result = errorService.getErrorDescription("E4004");
    // assertEquals("The requested resource was not found", result);
    // }

}
