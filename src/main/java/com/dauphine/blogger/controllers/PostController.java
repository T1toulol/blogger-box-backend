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
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) String date,
                                                      @RequestParam(required = false) String value) {
        return ResponseEntity.ok(postService.getAll(date, value));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostDto dto) {
        PostDto created = postService.create(dto);
        return ResponseEntity.created(URI.create("/posts/" + created.id())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody PostDto dto) {
        postService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPost(@PathVariable Long id, @RequestBody PostDto dto) {
        postService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Long>> getCategoriesByPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id).categoryIds());
    }
}
