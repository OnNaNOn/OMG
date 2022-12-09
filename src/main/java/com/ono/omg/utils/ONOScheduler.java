package com.ono.omg.utils;

import com.ono.omg.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ONOScheduler {

    private final SchedulerService schedulerService;

    public ONOScheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    /**
     * 매일 20시마다 다음날(오늘 + 1)이 시작일인 이벤트를 열기
     */
    @Scheduled(cron = "0 0 20 * * *")
    public void openEvent() {
        log.info("Change Open Event = {}", schedulerService.openEvent());
    }

    /**
     * 매일 01시마다 전날(오늘 -1)이 종료일인 이벤트를 닫기
     */
    @Scheduled(cron = "0 0 01 * * *")
    public void closeEvent() {
        log.info("Change Close Event = {}", schedulerService.closeEvent());
    }
}
