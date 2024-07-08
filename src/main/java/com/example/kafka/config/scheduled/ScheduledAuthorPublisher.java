package com.example.kafka.config.scheduled;

import com.example.kafka.interfaces.AuthorPublisherInterface;
import com.example.kafka.model.Author;
import com.example.kafka.model.Book;
import com.example.kafka.repositories.AuthorRepository;
import com.example.kafka.repositories.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ScheduledAuthorPublisher {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorPublisherInterface authorPublisherService;

    @KafkaListener(topics = "books", groupId = "author-publish-group")
    public void listenBookTopic(String bookMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Book book = objectMapper.readValue(bookMessage, Book.class);
            Optional<Book> fullBook = bookRepository.findById(book.getId());
            Author author = fullBook.get().getAuthor();
            if (author != null) {
                publishAuthor(author);
            } else {
                log.warn("Author is null for book {}", book.getTitle());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to process book message: {}", e.getMessage());
        }
    }

    public void publishAuthor(Author author) {
        try {
            authorPublisherService.publish(author);
        } catch (RuntimeException e) {
            log.warn("Failed to publish author {}", e.getMessage());
        }
    }
}
