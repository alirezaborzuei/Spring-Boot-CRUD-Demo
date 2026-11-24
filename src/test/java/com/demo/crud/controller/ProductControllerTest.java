package com.demo.crud.controller;

import com.demo.crud.dto.ProductRequest;
import com.demo.crud.dto.ProductResponse;
import com.demo.crud.service.ProductService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductResponse productResponse() {
        return new ProductResponse(
                1L,
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10,
                Instant.now(),
                Instant.now()
        );
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("POST /api/products should create product")
    void create_shouldReturnCreated() throws Exception {
        ProductRequest request = new ProductRequest(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10
        );
        ProductResponse response = productResponse();
        when(productService.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Laptop"));

        verify(productService).create(any(ProductRequest.class));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return product")
    void getById_shouldReturnProduct() throws Exception {
        ProductResponse response = productResponse();
        when(productService.getById(1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Laptop"));

        verify(productService).getById(1L);
    }

    @Test
    @DisplayName("GET /api/products should return all products")
    void getAll_shouldReturnList() throws Exception {
        when(productService.getAll()).thenReturn(Arrays.asList(productResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Laptop"));

        verify(productService).getAll();
    }

    @Test
    @DisplayName("PUT /api/products/{id} should update product")
    void update_shouldReturnUpdated() throws Exception {
        ProductRequest request = new ProductRequest(
                "Laptop Pro",
                "Updated",
                new BigDecimal("1500.00"),
                5
        );
        ProductResponse response = productResponse();
        when(productService.update(anyLong(), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(productService).update(anyLong(), any(ProductRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should delete product")
    void delete_shouldReturnOk() throws Exception {
        doNothing().when(productService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(productService).delete(1L);
    }
}
