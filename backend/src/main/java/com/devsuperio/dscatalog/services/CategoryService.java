package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dto.CategoryDTO;
import com.devsuperio.dscatalog.entities.Category;
import com.devsuperio.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}
