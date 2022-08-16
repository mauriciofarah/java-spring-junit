package com.devsuperio.dscatalog.repositories;

import com.devsuperio.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShoeldDeleteObjectWhenIdExists() {

        long existingId = 1L;

        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            long noExistingId = 999L;

            productRepository.deleteById(noExistingId);
        });
    }
}
