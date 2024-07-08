package com.example.kafka.exceptions;

import com.example.kafka.model.Book;
import lombok.Getter;

@Getter
public class BookPublishException extends RuntimeException {

    private final Book book;

    public BookPublishException(Book book) {
        this.book = book;
    }

    public BookPublishException(String message, final Book book) {
        super(message);
        this.book  = book;
    }

    public BookPublishException(Throwable cause, final Book book) {
        super(cause);
        this.book  = book;
    }

    public BookPublishException(String message, Throwable cause, final Book book) {
        super(message, cause);
        this.book  = book;
    }

    public BookPublishException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace, final Book book) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.book  = book;
    }


}
