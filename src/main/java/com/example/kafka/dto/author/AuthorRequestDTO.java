package com.example.kafka.dto.author;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AuthorRequestDTO {
    String first_name;
    String last_name;
    LocalDateTime birth_date;
}
