package com.example.kafka.services;

import com.example.kafka.exceptions.BookPublishException;
import com.example.kafka.interfaces.BookPublisherInterface;
import com.example.kafka.model.Author;
import com.example.kafka.model.Book;
import com.example.kafka.repositories.AuthorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookPublisherService implements BookPublisherInterface {

    private static final String TOPIC = "books";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate; // Inject KafkaTemplate

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void publish(Book book) {
        try {
            log.info("Publishing book {}", book.getTitle());
            kafkaTemplate.send(TOPIC, book);
            log.info("Book `{}` [{}] published", book.getTitle(), book.getId());
        } catch (RuntimeException e) {
            log.warn("Failed to publish book {}", e.getMessage());
        }
    }
}

