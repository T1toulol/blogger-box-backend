package com.dauphine.blogger.dto;

import java.time.Instant;
import java.util.List;

/**
 * Simple DTO for Post used to expose endpoints (stubbed / not implemented).
 */
public record PostDto(Long id, String title, String content, Instant createdDate, List<Long> categoryIds) {
}
