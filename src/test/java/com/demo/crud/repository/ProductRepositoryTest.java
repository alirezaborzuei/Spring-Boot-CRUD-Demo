package com.demo.crud.repository;

import com.demo.crud.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    @DisplayName("save and find product by id")
    void saveAndFindById() {
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(1200.0);
        product.setStock(10);

        Product saved = repository.save(product);
        assertThat(saved.getId()).isNotNull();

        Product found = repository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("delete product by id")
    void deleteById() {
        Product product = new Product();
        product.setName("Mouse");
        product.setDescription("Wireless mouse");
        product.setPrice(25.0);
        product.setStock(100);

        Product saved = repository.save(product);
        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("existsById returns true when product exists")
    void existsById() {
        Product product = new Product();
        product.setName("Keyboard");
        product.setDescription("Mechanical keyboard");
        product.setPrice(80.0);
        product.setStock(50);

        Product saved = repository.save(product);
        assertThat(repository.existsById(saved.getId())).isTrue();
    }
}
