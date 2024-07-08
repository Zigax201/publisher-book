package com.example.kafka.services;

import com.example.kafka.model.Author;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthorProducer {
    private static final String TOPIC = "authors";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Async
    public void sendMessage(Author author) {
        try {
            String payload = objectMapper.writeValueAsString(author);
            kafkaTemplate.send(TOPIC, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
