package com.example.kafka.interfaces;

import com.example.kafka.model.Author;

public interface AuthorPublisherInterface {
    void publish(Author author);
}
