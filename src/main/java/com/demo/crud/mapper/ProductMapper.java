package com.demo.crud.mapper;

import com.demo.crud.dto.ProductRequest;
import com.demo.crud.dto.ProductResponse;
import com.demo.crud.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(source = "price", target = "price")
    ProductResponse toResponse(Product product);
}
