package com.ono.omg.scheduler;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

    @Autowired
    EventRepository eventRepository;

//    @Scheduled(cron = "0 45 10 21 * *")
//    public void test() {
//
//        Event event = new Event("에어팟", 5000, "가전제품", "일반 배송", 100,11L );
//
//        eventRepository.saveAndFlush(event);
    }
