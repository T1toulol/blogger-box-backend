package com.dauphine.blogger.service.impl;

import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.exception.ResourceNotFoundException;
import com.dauphine.blogger.model.Category;
import com.dauphine.blogger.model.Post;
import com.dauphine.blogger.repository.PostRepository;
import com.dauphine.blogger.repository.CategoryRepository;
import com.dauphine.blogger.service.PostService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class JpaPostService implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public JpaPostService(PostRepository postRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<PostDto> getAll(String dateFilter, String value) {
        List<Post> list;
        if (value != null && !value.isBlank()) {
            list = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(value, value);
        } else {
            list = postRepository.findAll();
        }
        return list.stream()
                .filter(p -> dateFilter == null || p.getCreatedDate() == null || p.getCreatedDate().toString().startsWith(dateFilter))
                .sorted((a,b)->b.getCreatedDate().compareTo(a.getCreatedDate()))
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategory() == null ? List.of() : List.of(p.getCategory().getId())))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getById(Long id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        return new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategory() == null ? List.of() : List.of(p.getCategory().getId()));
    }

    @Override
    public PostDto create(PostDto dto) {
        Post p = new Post();
        p.setTitle(dto.title());
        p.setContent(dto.content());
        p.setCreatedDate(dto.createdDate() == null ? Instant.now() : dto.createdDate());
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Long cid = dto.categoryIds().get(0);
            Category c = categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + cid));
            p.setCategory(c);
        }
        Post saved = postRepository.save(p);
        return new PostDto(saved.getId(), saved.getTitle(), saved.getContent(), saved.getCreatedDate(), saved.getCategory() == null ? List.of() : List.of(saved.getCategory().getId()));
    }

    @Override
    public PostDto update(Long id, PostDto dto) {
        Post existing = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        existing.setTitle(dto.title());
        existing.setContent(dto.content());
        existing.setCreatedDate(dto.createdDate() == null ? existing.getCreatedDate() : dto.createdDate());
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Long cid = dto.categoryIds().get(0);
            Category c = categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + cid));
            existing.setCategory(c);
        } else {
            existing.setCategory(null);
        }
        Post saved = postRepository.save(existing);
        return new PostDto(saved.getId(), saved.getTitle(), saved.getContent(), saved.getCreatedDate(), saved.getCategory() == null ? List.of() : List.of(saved.getCategory().getId()));
    }

    @Override
    public void delete(Long id) {
        if (!postRepository.existsById(id)) throw new ResourceNotFoundException("Post not found: " + id);
        postRepository.deleteById(id);
    }

    @Override
    public List<PostDto> getByCategoryId(Long categoryId) {
        return postRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .sorted((a,b)->b.getCreatedDate().compareTo(a.getCreatedDate()))
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getCreatedDate(), p.getCategory() == null ? List.of() : List.of(p.getCategory().getId())))
                .collect(Collectors.toList());
    }
}
