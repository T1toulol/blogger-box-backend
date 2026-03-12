package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CategoryDto;
import com.dauphine.blogger.dto.PostDto;
import com.dauphine.blogger.service.CategoryService;
import com.dauphine.blogger.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final PostService postService;

    public CategoryController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(categoryService.getAll(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDto dto) {
        CategoryDto created = categoryService.create(dto);
        return ResponseEntity.created(URI.create("/categories/" + created.id())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        categoryService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        categoryService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getByCategoryId(id));
    }
}
