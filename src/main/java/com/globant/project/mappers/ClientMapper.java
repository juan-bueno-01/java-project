package com.globant.project.mappers;

import org.mapstruct.Mapper;

import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.entities.ClientEntity;

/**
 * ClientMapper
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity DtoToEntity(ClientDTO clientDto);

    ClientDTO EntityToDto(ClientEntity clientEntity);

}
