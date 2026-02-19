package com.dauphine.blogger.config;

import com.dauphine.blogger.dto.CategoryDto;
import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.service.CategoryService;
import com.dauphine.blogger.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryService categoryService;
    private final PostService postService;

    public DataLoader(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @Override
    public void run(String... args) {
        var c1 = categoryService.create(new CategoryDto(null, "Sport"));
        var c2 = categoryService.create(new CategoryDto(null, "Voyage"));

        postService.create(new PostDto(null, "Hello world", "First post content", Instant.now(), List.of(c1.id())));
        postService.create(new PostDto(null, "Travel tips", "How to travel cheap", Instant.now(), List.of(c2.id())));
    }
}
