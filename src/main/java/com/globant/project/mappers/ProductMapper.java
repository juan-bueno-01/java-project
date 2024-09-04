package com.globant.project.mappers;

import org.mapstruct.Mapper;

import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.ProductEntity;

/**
 * ProductMapper
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity DtoToEntity(ProductDTO productDto);

    ProductDTO EntityToDto(ProductEntity productEntity);

}
