package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    private long existingId;
    private long noExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;

        //Set Mockito to verify when delete service is called with existing id to do nothing;
        Mockito.doNothing().when(productRepository).deleteById(existingId);

        //Set Mockito to verify when delete service is called with nonexistent id to throw a EmptyResultDataAccessException;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(noExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        //Verify deleteProduct service doesn't throw an exception when successful;
        Assertions.assertDoesNotThrow(() -> productService.deleteProduct(existingId));

        //Set Mockito to check if the deleteById method is called and how many invocations have been made (1);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }
}
