package com.globant.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.entities.ClientEntity;

/**
 * ClientMapper
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "document", target = "document")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    ClientEntity DtoToEntity(ClientDTO clientDto);

    @Mapping(source = "document", target = "document")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    ClientDTO EntityToDto(ClientEntity clientEntity);

}
