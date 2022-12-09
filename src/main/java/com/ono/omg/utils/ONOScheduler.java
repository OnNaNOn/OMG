package com.ono.omg.utils;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.event.EventRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ONOScheduler {

    private final SchedulerService schedulerService;

    public ONOScheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    /**
     *
     */
    @Scheduled(cron = "0 0 20 * * *")
    public void openEvent() {
        log.info("Change Open Event = {}", schedulerService.openEvent());
    }

    @Scheduled(cron = "0 0 01 * * *")
    public void closeEvent() {
        log.info("Change Close Event = {}", schedulerService.closeEvent());
    }
}
