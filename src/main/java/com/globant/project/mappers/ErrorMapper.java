package com.globant.project.mappers;

import org.mapstruct.Mapper;

import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.domain.entities.ErrorEntity;

/**
 * ErrorMapper
 */
@Mapper(componentModel = "spring")
public interface ErrorMapper {

    ErrorEntity DtoToEntity(ErrorDTO errorDto);

    ErrorDTO EntityToDto(ErrorEntity errorEntity);

}
