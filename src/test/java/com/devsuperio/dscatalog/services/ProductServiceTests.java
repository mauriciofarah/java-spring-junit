package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.entities.Product;
import com.devsuperio.dscatalog.factory.Factory;
import com.devsuperio.dscatalog.repositories.ProductRepository;
import com.devsuperio.dscatalog.services.exceptions.DataBaseIntegrityException;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    private long existingId;
    private long noExistingId;
    private long dependentId;
    private Product product;
    private PageImpl<Product> page;
    private List<Product> productList;
    private Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;
        dependentId = 4L;
        product = Factory.createProduct();
        pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(List.of(product));
        productList = new ArrayList<>(List.of(product));

        //Simulate ProductRepository methods with Mockito

        Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.findAll()).thenReturn(productList);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.findById(noExistingId)).thenReturn(Optional.empty());

        //Set Mockito to verify when delete service is called with existing id to do nothing;
        Mockito.doNothing().when(productRepository).deleteById(existingId);

        //Set Mockito to verify when delete service is called with nonexistent id to throw a EmptyResultDataAccessException;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(noExistingId);

        //Set Mockito to verify when delete service is called whit violate database integrity and throw a DataIntegrityViolationException;
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPageOfProducts() {
        List<ProductDTO> result = productService.findAll();

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAll();
    }

    @Test
    public void findAllShouldReturnListOfProducts() {
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAll(pageable);
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
