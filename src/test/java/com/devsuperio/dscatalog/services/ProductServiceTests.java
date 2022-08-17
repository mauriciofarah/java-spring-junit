package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.repositories.ProductRepository;
import com.devsuperio.dscatalog.services.exceptions.DataBaseIntegrityException;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
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
    private long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;
        dependentId = 4L;

        //Simulate ProductRepository methods with Mockito

        //Set Mockito to verify when delete service is called with existing id to do nothing;
        Mockito.doNothing().when(productRepository).deleteById(existingId);

        //Set Mockito to verify when delete service is called with nonexistent id to throw a EmptyResultDataAccessException;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(noExistingId);

        //Set Mockito to verify when delete service is called whit violate database integrity and throw a DataIntegrityViolationException;
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        //Verify deleteProduct service doesn't throw an exception when successful;
        Assertions.assertDoesNotThrow(() -> productService.deleteProduct(existingId));

        //Set Mockito to check if the deleteById method is called and how many invocations have been made (1);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(noExistingId));

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(noExistingId);
    }

    @Test
    public void deleteShouldThrowDataBaseIntegrityExceptionWhenIdDependentId() {
        Assertions.assertThrows(DataBaseIntegrityException.class,
                () -> productService.deleteProduct(dependentId));

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
    }
}
