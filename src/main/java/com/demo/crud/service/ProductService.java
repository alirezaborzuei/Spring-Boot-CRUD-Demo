package com.demo.crud.service;

import com.demo.crud.dto.ProductRequest;
import com.demo.crud.dto.ProductResponse;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
