package com.dauphine.blogger.service.impl;

import com.dauphine.blogger.dto.CategoryDto;
import com.dauphine.blogger.exception.ResourceNotFoundException;
import com.dauphine.blogger.model.Category;
import com.dauphine.blogger.repository.CategoryRepository;
import com.dauphine.blogger.service.CategoryService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class JpaCategoryService implements CategoryService {

    private final CategoryRepository repository;

    public JpaCategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryDto> getAll(String nameFilter) {
        List<Category> list = (nameFilter == null || nameFilter.isBlank())
                ? repository.findAll()
                : repository.findByNameContainingIgnoreCase(nameFilter);
        return list.stream().map(c -> new CategoryDto(c.getId(), c.getName())).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return new CategoryDto(c.getId(), c.getName());
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.name());
        Category saved = repository.save(c);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        existing.setName(dto.name());
        Category saved = repository.save(existing);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Category not found: " + id);
        repository.deleteById(id);
    }
}
