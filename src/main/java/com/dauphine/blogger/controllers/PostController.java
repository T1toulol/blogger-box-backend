package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) String date) {
        return ResponseEntity.ok(postService.getAll(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto dto = postService.getById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostDto dto) {
        PostDto created = postService.create(dto);
        return ResponseEntity.created(URI.create("/posts/" + created.id())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody PostDto dto) {
        PostDto updated = postService.update(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPost(@PathVariable Long id, @RequestBody PostDto dto) {
        PostDto updated = postService.update(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Long>> getCategoriesByPost(@PathVariable Long id) {
        PostDto dto = postService.getById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto.categoryIds());
    }
}
