package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.CategoryDTO;
import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.entities.Category;
import com.devsuperio.dscatalog.entities.Product;
import com.devsuperio.dscatalog.repositories.CategoryRepository;
import com.devsuperio.dscatalog.repositories.ProductRepository;
import com.devsuperio.dscatalog.services.exceptions.DataBaseIntegrityException;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return new ProductDTO(
                product.orElseThrow(() -> new ResourceNotFoundException("Product not found")),
                product.get().getCategories()
        );
    }

    @Transactional
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = new Product();
        copyDtoToEntity(productDTO, product);
        product = productRepository.save(product);

        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(productDTO, product);
            product = productRepository.saveAndFlush(product);

            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseIntegrityException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product entityProduct) {
        entityProduct.setName(productDTO.getName());
        entityProduct.setDescription(productDTO.getDescription());
        entityProduct.setDate(productDTO.getDate());
        entityProduct.setImgUrl(productDTO.getImgUrl());
        entityProduct.setPrice(productDTO.getPrice());

        entityProduct.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            entityProduct.getCategories().add(category);
        }
    }
}
