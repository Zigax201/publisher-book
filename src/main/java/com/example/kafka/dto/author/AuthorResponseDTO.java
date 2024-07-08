package com.example.kafka.dto.author;

import com.example.kafka.model.Book;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class AuthorResponseDTO {
    Long id;
    String first_name;
    String last_name;
    LocalDateTime birth_date;
    List<Book> list_book;
}
