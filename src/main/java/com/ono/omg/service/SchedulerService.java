package com.ono.omg.service;

import com.ono.omg.domain.Event;
import com.ono.omg.repository.event.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SchedulerService {
    private final EventRepository eventRepository;

    public SchedulerService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Integer openEvent() {
        int openEventCount = 0;

        List<Event> events = eventRepository.findBySoldTypeIsFalse();

        String now = LocalDateTime.now().toString().substring(0, 10);

        for (Event event : events) {
            // 이벤트 시작일이 (오늘 + 1일)인 이벤트의 판매 여부를 True 로 변경
            if (now.matches(event.getStartedAt().minusDays(1).toString().substring(0, 10))) {
                event.changeSoldTypeIsTrue();
                openEventCount++;
            }
        }
        return openEventCount;
    }

    public Integer closeEvent() {
        int closeEventCount = 0;

        List<Event> events = eventRepository.findBySoldTypeIsTrue();

        // 이벤트 종료일 (오늘 + 1일)인 이벤트의 판매 여부를 False 로 변경
        String now = LocalDateTime.now().toString().substring(0, 10);

        System.out.println("now = " + now);

        for (Event event : events) {
            String endDate = event.getEndedAt().plusDays(1).toString().substring(0, 10);

            System.out.println("endDate = " + endDate);
            if (now.matches(endDate)) {
                event.changeSoldTypeIsFalse();
                closeEventCount++;
            }
        }
        return closeEventCount;
    }
}
