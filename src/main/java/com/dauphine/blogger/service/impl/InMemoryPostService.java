package com.dauphine.blogger.service.impl;

import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.model.Category;
import com.dauphine.blogger.model.Post;
import com.dauphine.blogger.service.CategoryService;
import com.dauphine.blogger.service.PostService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InMemoryPostService implements PostService {

    private final Map<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);
    private final CategoryService categoryService;

    public InMemoryPostService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private List<Long> toCategoryIds(Post p) {
        return p.getCategory() == null ? List.of() : List.of(p.getCategory().getId());
    }

    private Category findCategoryFromDto(PostDto dto) {
        if (dto.categoryIds() == null || dto.categoryIds().isEmpty()) return null;
        Long catId = dto.categoryIds().get(0);
        var cdto = categoryService.getById(catId);
        if (cdto == null) return null;
        // rebuild Category model for in-memory storage
        return new Category(cdto.id(), cdto.name());
    }

    @Override
    public List<PostDto> getAll(String dateFilter, String value) {
    return store.values().stream()
        .filter(p -> (dateFilter == null || p.getCreatedDate().toString().startsWith(dateFilter))
            && (value == null || p.getTitle().toLowerCase().contains(value.toLowerCase()) || (p.getContent() != null && p.getContent().toLowerCase().contains(value.toLowerCase()))))
        .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
        .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), toCategoryIds(p)))
        .collect(Collectors.toList());
    }

    @Override
    public PostDto getById(Long id) {
        Post p = store.get(id);
        if (p == null) return null;
        return new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), toCategoryIds(p));
    }

    @Override
    public PostDto create(PostDto dto) {
        Long id = seq.getAndIncrement();
        Instant created = dto.createdDate() == null ? Instant.now() : dto.createdDate();
        Category category = findCategoryFromDto(dto);
        Post p = new Post(id, dto.title(), dto.content(), created, category);
        store.put(id, p);
        return new PostDto(id, p.getTitle(), p.getContent(), p.getCreatedDate(), toCategoryIds(p));
    }

    @Override
    public PostDto update(Long id, PostDto dto) {
        Post existing = store.get(id);
        if (existing == null) return null;
        existing.setTitle(dto.title());
        existing.setContent(dto.content());
        existing.setCreatedDate(dto.createdDate() == null ? existing.getCreatedDate() : dto.createdDate());
        Category category = findCategoryFromDto(dto);
        existing.setCategory(category);
        return new PostDto(existing.getId(), existing.getTitle(), existing.getContent(), existing.getCreatedDate(), toCategoryIds(existing));
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public List<PostDto> getByCategoryId(Long categoryId) {
        return store.values().stream()
                .filter(p -> p.getCategory() != null && Optional.ofNullable(p.getCategory().getId()).map(i -> i.equals(categoryId)).orElse(false))
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), toCategoryIds(p)))
                .collect(Collectors.toList());
    }
}
