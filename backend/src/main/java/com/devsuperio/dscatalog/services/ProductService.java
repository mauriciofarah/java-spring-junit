package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.entities.Product;
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

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = repository.findAll();
        return list.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return new ProductDTO(
                product.orElseThrow(() -> new ResourceNotFoundException("Product not found")),
                product.get().getCategories()
        );
    }

    //TO BE IMPLEMENTED
//    @Transactional(readOnly = true)
//    public ProductDTO saveProduct(ProductDTO productDTO) {
//        Product product = new Product();
//        product.setName(productDTO.getName());
//        product = repository.save(product);
//
//        return new ProductDTO(product);
//    }

    //TO BE IMPLEMENTED
//    @Transactional
//    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
//        try {
//            Product product = repository.getReferenceById(id);
//            product.setName(productDTO.getName());
//            product = repository.saveAndFlush(product);
//
//            return new ProductDTO(product);
//        } catch (EntityNotFoundException e) {
//            throw new ResourceNotFoundException("Id not found " + id);
//        }
//    }

    //TO BE IMPLEMENTED
//    public void deleteProduct(Long id) {
//        try {
//            repository.deleteById(id);
//        } catch (EmptyResultDataAccessException e) {
//            throw new ResourceNotFoundException("Id not found " + id);
//        } catch (DataIntegrityViolationException e ) {
//            throw new DataBaseIntegrityException("Integrity violation");
//        }
//    }
}
