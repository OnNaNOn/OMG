package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.request.EventRequestDto.CreateEventDto;
import static com.ono.omg.dto.response.EventResponseDto.AllEventResponse;

// 처리되어있는 것들은 모두 Redisson 관련코드임

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<AllEventResponse> searchEvent() {
        List<Event> events = eventRepository.findBySoldTypeIsTrue();
        List<AllEventResponse> responseDto = new ArrayList<>();

        for (Event event : events) {
            Product findProduct = productRepository.findById(event.getProductId()).get();
            responseDto.add(new AllEventResponse(event, findProduct));
        }
        return responseDto;
    }

    @Transactional
    public AllEventResponse createEvent(CreateEventDto createEventDto, Account account) {
        Product saveProduct = productRepository.save(new Product(
                createEventDto.getProductName(),
                createEventDto.getProductPrice(),
                "이벤트",
                "초고속 배송",
                createEventDto.getProductStock(),
                account.getId()
                )
        );

        Event saveEvent = eventRepository.save(new Event(
                saveProduct.getId(),
                createEventDto.getEventName(),
                createEventDto.getEventDesc(),
                createEventDto.getStartDate().plusHours(9),
                createEventDto.getStartDate().plusDays(1).minusHours(9)
        ));

        System.out.println("getStartedAt = " + saveEvent.getStartedAt());
        System.out.println("getEndedAt = " + saveEvent.getEndedAt());

        return new AllEventResponse(saveEvent, saveProduct);
    }
}
