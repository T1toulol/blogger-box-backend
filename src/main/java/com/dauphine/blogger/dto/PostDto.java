package com.dauphine.blogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.List;

/**
 * Simple DTO for Post used to expose endpoints.
 */
public record PostDto(Long id, String title, String content,
					  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC") Instant createdDate,
					  List<Long> categoryIds) {
}
