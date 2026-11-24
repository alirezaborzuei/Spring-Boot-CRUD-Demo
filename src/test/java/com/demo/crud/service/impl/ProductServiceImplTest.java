package com.demo.crud.service.impl;

import com.demo.crud.dto.ProductRequest;
import com.demo.crud.dto.ProductResponse;
import com.demo.crud.entity.Product;
import com.demo.crud.exception.ResourceNotFoundException;
import com.demo.crud.event.ProductEventPublisher;
import com.demo.crud.mapper.ProductMapper;
import com.demo.crud.repository.ProductRepository;
import com.demo.crud.service.ProductService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductEventPublisher eventPublisher;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest request;
    private Product entity;
    private Product savedEntity;
    private ProductResponse response;

    @BeforeEach
    void setUp() {
        request = new ProductRequest(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10
        );

        entity = new Product();
        entity.setId(1L);
        entity.setName("Laptop");
        entity.setDescription("Gaming laptop");
        entity.setPrice(1200.0);
        entity.setStock(10);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        savedEntity = new Product();
        savedEntity.setId(1L);
        savedEntity.setName("Laptop");
        savedEntity.setDescription("Gaming laptop");
        savedEntity.setPrice(1200.0);
        savedEntity.setStock(10);

        response = new ProductResponse(
                1L,
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    @DisplayName("create should save product and publish created event")
    void create_shouldSaveAndPublishEvent() {
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(response);

        ProductService underTest = productService;
        ProductResponse result = underTest.create(request);

        assertThat(result).isEqualTo(response);
        verify(repository).save(entity);
        verify(eventPublisher).publishCreated(1L);
    }

    @Test
    @DisplayName("getById should return product response when found")
    void getById_shouldReturnResponseWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        ProductService underTest = productService;
        ProductResponse result = underTest.getById(1L);

        assertThat(result).isEqualTo(response);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("getById should throw ResourceNotFoundException when not found")
    void getById_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductService underTest = productService;
        assertThatThrownBy(() -> underTest.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id: 99");
    }

    @Test
    @DisplayName("getAll should return list of product responses")
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(Arrays.asList(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        ProductService underTest = productService;
        List<ProductResponse> result = underTest.getAll();

        assertThat(result).hasSize(1).containsExactly(response);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("update should modify product and publish updated event")
    void update_shouldModifyAndPublishEvent() {
        ProductRequest updateRequest = new ProductRequest(
                "Laptop Pro",
                "Updated gaming laptop",
                new BigDecimal("1500.00"),
                5
        );
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Product.class))).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(response);

        ProductService underTest = productService;
        ProductResponse result = underTest.update(1L, updateRequest);

        assertThat(result).isEqualTo(response);
        verify(eventPublisher).publishUpdated(1L);
    }

    @Test
    @DisplayName("update should throw when product not found")
    void update_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductService underTest = productService;
        assertThatThrownBy(() -> underTest.update(99L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete should remove product and publish deleted event")
    void delete_shouldRemoveAndPublishEvent() {
        when(repository.existsById(1L)).thenReturn(true);

        ProductService underTest = productService;
        underTest.delete(1L);

        verify(repository).deleteById(1L);
        verify(eventPublisher).publishDeleted(1L);
    }

    @Test
    @DisplayName("delete should throw when product not found")
    void delete_shouldThrowWhenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        ProductService underTest = productService;
        assertThatThrownBy(() -> underTest.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).deleteById(99L);
        verify(eventPublisher, never()).publishDeleted(99L);
    }
}
