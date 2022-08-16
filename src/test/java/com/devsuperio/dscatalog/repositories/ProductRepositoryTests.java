package com.devsuperio.dscatalog.repositories;

import com.devsuperio.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long noExistingId;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;
    }

    @Test
    public void deleteShoeldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(noExistingId);
        });
    }
}
