package com.ono.omg.utils;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
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

    @Scheduled(cron = "50 00 19 * * *") // 21-30일까지 02시 13분 55초에 스케줄 동작 >> cron = "55 13 02 21-30 * *"
    public void createEvent() {
        Product savedProduct = productRepository.save(new Product("에어팟 프로2", 5000, "이벤트 상품", "초고속 배송", 500000, 1L));
        LocalDateTime now = LocalDateTime.now();

        eventRepository.saveAndFlush(new Event(savedProduct.getId(), "매일 매일 쏟아지는 에어팟!", "야 나두", now, now.plusDays(1)));
    }
}
