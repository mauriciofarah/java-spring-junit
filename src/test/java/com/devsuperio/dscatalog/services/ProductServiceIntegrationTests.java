package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.repositories.ProductRepository;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
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

    @Test
    public void findAllShouldReturnAllProducts() {
        List<ProductDTO> result = productService.findAll();

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countTotalProducts, result.size());
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size12() {
        PageRequest pageRequest = PageRequest.of(0, 12);

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(12, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(99, 12);

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortedByName() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("name"));

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
