package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.CategoryDTO;
import com.devsuperio.dscatalog.entities.Category;
import com.devsuperio.dscatalog.repositories.CategoryRepository;
import com.devsuperio.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(item -> new CategoryDTO(item)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> category = repository.findById(id);
        return new CategoryDTO(category.orElseThrow(() -> new EntityNotFoundException("Category not found")));
    }
}
