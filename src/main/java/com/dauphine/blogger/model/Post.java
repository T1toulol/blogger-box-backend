package com.dauphine.blogger.model;

import java.time.Instant;
import java.util.List;

public class Post {
    private Long id;
    private String title;
    private String content;
    private Instant createdDate;
    private List<Long> categoryIds;

    public Post() {}

    public Post(Long id, String title, String content, Instant createdDate, List<Long> categoryIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryIds = categoryIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedDate() { return createdDate; }
    public void setCreatedDate(Instant createdDate) { this.createdDate = createdDate; }

    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
}
