package com.ono.omg.scheduler;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.product.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ONOScheduler {

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    public ONOScheduler(EventRepository eventRepository, ProductRepository productRepository) {
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
    }

    @Scheduled(cron = "30 51 21 21-30 * *")
    public void test() {

        productRepository.save(new Product("상품테스트", 10000, "카테고리", "빠름", 150, 1L));

        long rand = (long)(Math.random() * productRepository.count()) + 1;

        LocalDateTime dateTime = LocalDateTime.now();

        Product findProduct = productRepository.findById(rand).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        Event event = new Event(
                findProduct.getId(),
                "30일까지 9일간 매일 20시 41분에는 행사 상품이!!",
                "상품은 무려 에어팟..!",
                5000,
                10000L,
                dateTime, dateTime.plusDays(1)
        );

        eventRepository.saveAndFlush(event);
    }
}
