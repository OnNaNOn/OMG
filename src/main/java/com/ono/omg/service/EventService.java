package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Event;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.OrderResponseDto.EventOrderResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final RedissonClient redissonClient;

    public EventOrderResponseDto eventOrder(Long eventId, Account account) {
        RLock lock = redissonClient.getLock(eventId.toString());

        EventOrderResponseDto eventOrderResponseDto;

        try {
            // 몇 초동안 점유할 것인지에 대한 설정
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            // 점유하지 못한 경우
            if(!available) {
                System.out.println("lock 획득 실패");
                throw new RuntimeException("락 획득 실패");
            }

            // lock 획득 성공
            eventOrderResponseDto = createEventOrderWithRedisson(eventId, account.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 락 해제
            lock.unlock();
        }
        return eventOrderResponseDto;
    }

    @Transactional
    protected EventOrderResponseDto createEventOrderWithRedisson(Long eventId, Long accountId) {
        Event findEvent = validEvent(eventId);
        Product findProduct = validProduct(findEvent);
        Account findAccount = validAccount(accountId);

        findEvent.decreaseEventStock(1);
        eventRepository.save(findEvent);
        System.out.println("findEvent.getMaxParticipant() = " + findEvent.getMaxParticipant());

        Order savedOrder = orderRepository.save(new Order(findAccount, findProduct, findEvent.getProductId(), findEvent.getProductPrice()));

        return new EventOrderResponseDto(savedOrder.getId(), findEvent.getId(), findAccount.getUsername(), findEvent.getEventTitle());
    }

    private Product validProduct(Event findEvent) {
        return productRepository.findById(findEvent.getProductId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }


    private Event validEvent(Long eventId) {
        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );
        return findEvent;
    }

    private Account validAccount(Long accountId) {
        Account findAccount = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        return findAccount;
    }



}
