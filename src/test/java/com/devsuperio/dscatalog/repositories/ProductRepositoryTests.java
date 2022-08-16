package com.devsuperio.dscatalog.repositories;

import com.devsuperio.dscatalog.entities.Product;
import com.devsuperio.dscatalog.factory.Factory;
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
    //Total product elements initially instantiated
    private long countTotalProducts;
    private Product product;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;
        countTotalProducts = 25L;
        product = Factory.createProduct();
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotNullOptionalWhenObjectIsFound() {
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent() && result.get().getId() == existingId);
    }

    @Test
    public void findByIdShouldReturnNullWhenObjectIsNotFound() {
        Optional<Product> result = productRepository.findById(noExistingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> productRepository.deleteById(noExistingId));
    }
}
