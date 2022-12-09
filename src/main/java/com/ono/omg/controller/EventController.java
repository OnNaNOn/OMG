package com.ono.omg.controller;


import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ono.omg.dto.request.EventRequestDto.CreateEventDto;
import static com.ono.omg.dto.response.EventResponseDto.AllEventResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    // 이벤트 상품 주문하기
    private final EventService eventService;

    @GetMapping("/event")
    public ResponseDto<List<AllEventResponse>> findEvents() {
        return ResponseDto.success(eventService.searchEvent());
    }

    @PostMapping("/event")
    public ResponseEntity<AllEventResponse> createEvent(@RequestBody CreateEventDto createEventDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(eventService.createEvent(createEventDto, userDetails.getAccount()));
    }
}
