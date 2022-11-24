package com.ono.omg.scheduler;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestScheduler {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ProductRepository productRepository;

    @Scheduled(cron = "0 41 20 21-30 * *")
    public void test() {

        long rand = (long)(Math.random() * productRepository.count()) + 1;

        LocalDateTime dateTime = LocalDateTime.now();

        Product findProduct = productRepository.findById(rand).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        Event event = new Event(findProduct.getTitle(), "깜짝세일", "오늘만 특가예요", 100L, dateTime, dateTime.plusDays(1));
        eventRepository.saveAndFlush(event);
    }
}
