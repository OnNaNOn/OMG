package com.ono.omg.scheduler;

import com.ono.omg.domain.Product;
import com.ono.omg.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestScheduler {

    @Autowired
    ProductRepository productRepository;

    @Scheduled(cron = "0 50 14 21-30 * *")
    public void test() {

        Product product = new Product("에어팟", 5000, "가전제품", "일반 배송", 100,11L );

        productRepository.saveAndFlush(product);
    }
}
