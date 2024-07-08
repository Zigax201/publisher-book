package com.example.kafka.dto.book;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookResponseDTO {
    private String title;
}
