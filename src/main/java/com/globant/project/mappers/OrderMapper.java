package com.globant.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.entities.OrderEntity;

/**
 * DeliveryMapper
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "clientDocument.document", source = "clientDocument")
    @Mapping(target = "productUuid.uuid", source = "productUuid")
    OrderEntity DtoToEntity(OrderDTO orderDTO);

    @Mapping(target = "clientDocument", source = "clientDocument.document")
    @Mapping(target = "productUuid", source = "productUuid.uuid")
    OrderDTO EntityToDto(OrderEntity orderEntity);

}
