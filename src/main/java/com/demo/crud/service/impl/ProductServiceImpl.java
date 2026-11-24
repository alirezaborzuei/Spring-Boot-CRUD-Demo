package com.demo.crud.service.impl;

import com.demo.crud.dto.ProductRequest;
import com.demo.crud.dto.ProductResponse;
import com.demo.crud.entity.Product;
import com.demo.crud.exception.ResourceNotFoundException;
import com.demo.crud.event.ProductEventPublisher;
import com.demo.crud.mapper.ProductMapper;
import com.demo.crud.repository.ProductRepository;
import com.demo.crud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductEventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse create(ProductRequest request) {
        Product entity = mapper.toEntity(request);
        Product saved = repository.save(entity);
        eventPublisher.publishCreated(saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "products", key = "'all'")
    public ProductResponse update(Long id, ProductRequest request) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice().doubleValue());
        entity.setStock(request.getStock());
        entity.setUpdatedAt(java.time.Instant.now());
        Product saved = repository.save(entity);
        eventPublisher.publishUpdated(saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        repository.deleteById(id);
        eventPublisher.publishDeleted(id);
    }
}
