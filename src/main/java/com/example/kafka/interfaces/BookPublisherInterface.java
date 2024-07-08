package com.example.kafka.interfaces;

import com.example.kafka.model.Book;

public interface BookPublisherInterface {
    void publish(Book book);
}
