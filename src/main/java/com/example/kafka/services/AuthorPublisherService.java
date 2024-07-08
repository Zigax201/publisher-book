package com.example.kafka.services;

import com.example.kafka.interfaces.AuthorPublisherInterface;
import com.example.kafka.model.Author;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorPublisherService implements AuthorPublisherInterface {
    private static final String TOPIC = "authors";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void publish(Author author) {
        try {
            log.info("Publishing author {} {}", author.getFirst_name(), author.getLast_name());
            String payload = objectMapper.writeValueAsString(author.getId());
            kafkaTemplate.send(TOPIC, payload);
            log.info("Author `{}` [{}] published", author.getFirst_name(), author.getId());
        } catch (RuntimeException e) {
            log.warn("Failed to publish author {}", e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
