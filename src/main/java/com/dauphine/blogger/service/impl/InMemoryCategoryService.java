package com.dauphine.blogger.service.impl;

import com.dauphine.blogger.dto.CategoryDto;
import com.dauphine.blogger.model.Category;
import com.dauphine.blogger.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InMemoryCategoryService implements CategoryService {

    private final Map<Long, Category> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public List<CategoryDto> getAll() {
        return store.values().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category c = store.get(id);
        if (c == null) return null;
        return new CategoryDto(c.getId(), c.getName());
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        Long id = seq.getAndIncrement();
        Category c = new Category(id, dto.name());
        store.put(id, c);
        return new CategoryDto(id, c.getName());
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category existing = store.get(id);
        if (existing == null) return null;
        existing.setName(dto.name());
        return new CategoryDto(existing.getId(), existing.getName());
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
