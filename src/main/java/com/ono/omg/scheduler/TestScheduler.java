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

    @Scheduled(cron = "0 45 10 21-30 * *")
    public void test() {

//        int rand = (int)(Math.random() * 10000) + 1;
//
//        Product findProduct = productRepository.findById(rand).orElseThrow(
//                ()
//        );
//
//        // 현재 시간 = now로 얻어오고 이벤트 종료일은 +1일 or 6시간뒤 마감
//        new Event(findProduct.getId(), now() + 1Day, 가격 = 5000, 최대 참여자수 = 10000 (=이벤트 재고), ... )
//        new Event(findProduct.getId(), now() + 1Day, 가격 = 5000, 최대 참여자수 = findProduct.getStock()), ... )

        // 1. 에어팟을 가지고 이벤트를 하려고한다.
        // 2. 이벤트 내용: 에어팟 => 선착순 5000명에게 5000원에 판매한다.
        // 조건: 에어팟의 재고가 5000개(or 5000개 이상) 여야한다.
        // 3. 현재 판매되는 일반 에어팟 상품이 재고가 300개라면?? Prduct.getStock()
        // 5000개의 재고가 필요한 상황인데 현재 에어팟의 재고가 300개라면 ==> ????????

        // 정말 최악의 경우라면 재고가 0 or 1개인거나 >> 300, 5000
        // 일반 에어팟 20만원 재고가 300
        // 에어팟, 판매여부, 이미지 사진

        // ======================

        // 100개 한정 ~~~~~

        // 일반 판매되는 에어팟 재고
        // 이벤트 되는 에어팟의 재고

        // ================

        // 이벤트가 될 때 에어팟의  현재 재고를 123개 >> 5000
        //    setProduct = 5000;

//        Product product = new Product("에어팟", 5000, "가전제품", "일반 배송", 100, 11L);
//
//        productRepository.saveAndFlush(product);
//
//    }
//}
//    @Scheduled(cron = "0 45 10 21 * *")
//    public void test() {
//dfdsaf
//        Event event = new Event("에어팟", 5000, "가전제품", "일반 배송", 100,11L );
//
//        eventRepository.saveAndFlush(event);
    }
}
