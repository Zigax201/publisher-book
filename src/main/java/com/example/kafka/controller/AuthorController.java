package com.example.kafka.controller;

import com.example.kafka.dto.author.AuthorResponseDTO;
import com.example.kafka.dto.book.BookResponseDTO;
import com.example.kafka.dto.exception.ExceptionResponseDTO;
import com.example.kafka.interfaces.BookPublisherInterface;
import com.example.kafka.model.Author;
import com.example.kafka.model.Book;
import com.example.kafka.repositories.AuthorRepository;
import com.example.kafka.services.AuthorProducer;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorProducer authorProducer;

    @Autowired
    private BookPublisherInterface bookPublisherService;

    @PostMapping("/authors")
    public ResponseEntity createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        authorProducer.sendMessage(savedAuthor);
        return ResponseEntity.ok(savedAuthor);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity getAuthorById(@PathVariable Long authorId) {
        try {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            return ResponseEntity.ok(AuthorResponseDTO.builder()
                    .id(author.getId())
                    .first_name(author.getFirst_name())
                    .last_name(author.getLast_name())
                    .birth_date(author.getBirth_date())
                    .list_book(author.getBooks())
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ExceptionResponseDTO.builder()
                    .message(e.getMessage())
                    .type("application/json")
                    .httpStatus(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ExceptionResponseDTO.builder()
                    .message(e.getMessage())
                    .type("application/json")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/authors/{authorId}/books")
    public ResponseEntity createBook(@PathVariable Long authorId, @RequestBody Book book) {
        try {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            book.setAuthor(author);
            author.getBooks().add(book);
            authorRepository.save(author);
            return ResponseEntity.ok(BookResponseDTO.builder().title(book.getTitle()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ExceptionResponseDTO.builder()
                    .message(e.getMessage())
                    .type("application/json")
                    .httpStatus(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ExceptionResponseDTO.builder()
                    .message(e.getMessage())
                    .type("application/json")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/authors")
    public ResponseEntity<?> getAllAuthors() {
        try {
            List<AuthorResponseDTO> authors = authorRepository.findAll().stream()
                    .map(author -> AuthorResponseDTO.builder()
                            .id(author.getId())
                            .first_name(author.getFirst_name())
                            .last_name(author.getLast_name())
                            .birth_date(author.getBirth_date())
                            .list_book(author.getBooks())
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ExceptionResponseDTO.builder()
                    .message(e.getMessage())
                    .type("application/json")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }
}

