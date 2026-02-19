package com.dauphine.blogger.service.impl;

import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.model.Post;
import com.dauphine.blogger.service.PostService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InMemoryPostService implements PostService {

    private final Map<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public List<PostDto> getAll(String dateFilter) {
        return store.values().stream()
                .filter(p -> dateFilter == null || p.getCreatedDate().toString().startsWith(dateFilter))
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategoryIds()))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getById(Long id) {
        Post p = store.get(id);
        if (p == null) return null;
        return new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategoryIds());
    }

    @Override
    public PostDto create(PostDto dto) {
        Long id = seq.getAndIncrement();
        Instant created = dto.createdDate() == null ? Instant.now() : dto.createdDate();
        Post p = new Post(id, dto.title(), dto.content(), created, dto.categoryIds());
        store.put(id, p);
        return new PostDto(id, p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategoryIds());
    }

    @Override
    public PostDto update(Long id, PostDto dto) {
        Post existing = store.get(id);
        if (existing == null) return null;
        existing.setTitle(dto.title());
        existing.setContent(dto.content());
        existing.setCategoryIds(dto.categoryIds());
        existing.setCreatedDate(dto.createdDate() == null ? existing.getCreatedDate() : dto.createdDate());
        return new PostDto(existing.getId(), existing.getTitle(), existing.getContent(), existing.getCreatedDate(), existing.getCategoryIds());
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public List<PostDto> getByCategoryId(Long categoryId) {
        return store.values().stream()
                .filter(p -> p.getCategoryIds() != null && p.getCategoryIds().contains(categoryId))
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategoryIds()))
                .collect(Collectors.toList());
    }
}
