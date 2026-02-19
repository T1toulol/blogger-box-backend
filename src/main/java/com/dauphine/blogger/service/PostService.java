package com.dauphine.blogger.service;

import com.dauphine.blogger.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAll(String dateFilter);
    PostDto getById(Long id);
    PostDto create(PostDto dto);
    PostDto update(Long id, PostDto dto);
    void delete(Long id);
    List<PostDto> getByCategoryId(Long categoryId);
}
