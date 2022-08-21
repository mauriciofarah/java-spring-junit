package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.repositories.ProductRepository;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIntegrationTests {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    private long existingId;
    private long noExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        noExistingId = 999L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.deleteProduct(existingId);

        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(noExistingId));
    }
}
