package com.globant.project.application.mappers.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.globant.project.application.mappers.ErrorMapper;
import com.globant.project.application.mappers.ErrorMapperImpl;
import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.domain.entities.ErrorEntity;

/**
 * ErrorMapperUnitTest
 */
public class ErrorMapperUnitTest {

    private final ErrorMapper errorMapper = new ErrorMapperImpl();

    @Test
    public void testDtoToEntity() {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorCode("E4004");
        errorDTO.setErrorType("Not_Found_Error");
        errorDTO.setDescription("Not Found Exception");
        errorDTO.setCreatedAt(LocalDateTime.now());
        errorDTO.setUpdatedAt(LocalDateTime.now());

        ErrorEntity errorEntity = errorMapper.DtoToEntity(errorDTO);

        assertEquals(errorDTO.getErrorCode(), errorEntity.getErrorCode());
        assertEquals(errorDTO.getErrorType(), errorEntity.getErrorType());
        assertEquals(errorDTO.getDescription(), errorEntity.getDescription());
        assertEquals(errorDTO.getCreatedAt(), errorEntity.getCreatedAt());
        assertEquals(errorDTO.getUpdatedAt(), errorEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDto() {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setErrorCode("E4004");
        errorEntity.setErrorType("Not_Found_Error");
        errorEntity.setDescription("Not Found Exception");
        errorEntity.setCreatedAt(LocalDateTime.now());
        errorEntity.setUpdatedAt(LocalDateTime.now());

        ErrorDTO errorDTO = errorMapper.EntityToDto(errorEntity);

        assertEquals(errorEntity.getErrorCode(), errorDTO.getErrorCode());
        assertEquals(errorEntity.getErrorType(), errorDTO.getErrorType());
        assertEquals(errorEntity.getDescription(), errorDTO.getDescription());
        assertEquals(errorEntity.getCreatedAt(), errorDTO.getCreatedAt());
        assertEquals(errorEntity.getUpdatedAt(), errorDTO.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNull() {
        ErrorEntity errorEntity = new ErrorEntity();

        ErrorDTO errorDTO = errorMapper.EntityToDto(errorEntity);

        assertEquals(errorEntity.getErrorCode(), errorDTO.getErrorCode());
        assertEquals(errorEntity.getErrorType(), errorDTO.getErrorType());
        assertEquals(errorEntity.getDescription(), errorDTO.getDescription());
        assertEquals(errorEntity.getCreatedAt(), errorDTO.getCreatedAt());
        assertEquals(errorEntity.getUpdatedAt(), errorDTO.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNull() {
        ErrorDTO errorDTO = new ErrorDTO();

        ErrorEntity errorEntity = errorMapper.DtoToEntity(errorDTO);

        assertEquals(errorDTO.getErrorCode(), errorEntity.getErrorCode());
        assertEquals(errorDTO.getErrorType(), errorEntity.getErrorType());
        assertEquals(errorDTO.getDescription(), errorEntity.getDescription());
        assertEquals(errorDTO.getCreatedAt(), errorEntity.getCreatedAt());
        assertEquals(errorDTO.getUpdatedAt(), errorEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNullValues() {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setErrorCode("E4004");

        ErrorDTO errorDTO = errorMapper.EntityToDto(errorEntity);

        assertEquals(errorEntity.getErrorCode(), errorDTO.getErrorCode());
        assertEquals(errorEntity.getErrorType(), errorDTO.getErrorType());
        assertEquals(errorEntity.getDescription(), errorDTO.getDescription());
        assertEquals(errorEntity.getCreatedAt(), errorDTO.getCreatedAt());
        assertEquals(errorEntity.getUpdatedAt(), errorDTO.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNullValues() {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorCode("E4004");

        ErrorEntity errorEntity = errorMapper.DtoToEntity(errorDTO);

        assertEquals(errorDTO.getErrorCode(), errorEntity.getErrorCode());
        assertEquals(errorDTO.getErrorType(), errorEntity.getErrorType());
        assertEquals(errorDTO.getDescription(), errorEntity.getDescription());
        assertEquals(errorDTO.getCreatedAt(), errorEntity.getCreatedAt());
        assertEquals(errorDTO.getUpdatedAt(), errorEntity.getUpdatedAt());
    }

    @Test
    public void testNullEntityToDto() {
        ErrorEntity errorEntity = null;

        ErrorDTO errorDTO = errorMapper.EntityToDto(errorEntity);

        assertEquals(errorEntity, errorDTO);
    }

    @Test
    public void testNullDtoToEntity() {
        ErrorDTO errorDTO = null;

        ErrorEntity errorEntity = errorMapper.DtoToEntity(errorDTO);

        assertEquals(errorDTO, errorEntity);
    }

}
