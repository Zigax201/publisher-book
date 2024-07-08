package com.example.kafka.config.scheduled;

import com.example.kafka.interfaces.BookPublisherInterface;
import com.example.kafka.repositories.BookRepository;
import com.example.kafka.services.BookPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledBookPublisher {

    private long counter;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookPublisherInterface bookPublisherService;

    @Scheduled(cron = "0/20 * * * * *")
    public void publishBooks() {
        bookRepository.findById(counter).ifPresentOrElse(book -> {
            counter += 1;
            bookPublisherService.publish(book);
        }, this::resetCounter);
    }

    private void resetCounter(){
        this.counter = 1;
        log.info("Counter reset to 1");
    }
}
