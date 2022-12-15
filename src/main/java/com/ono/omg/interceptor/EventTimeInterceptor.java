package com.ono.omg.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ono.omg.domain.Event;
import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.event.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class EventTimeInterceptor implements HandlerInterceptor {

    private final EventRepository eventRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public EventTimeInterceptor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (!requestURI.contains("confirm")) {
            return true;
        }

        Long productId = Long.valueOf(requestURI.split("/")[3]);

        Optional<Event> findEvent = eventRepository.findByProductId(productId);

        if(findEvent.isEmpty()) {
            return true;
        }
        Event event = findEvent.get();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = event.getStartedAt();
        LocalDateTime end = event.getEndedAt();

        if(now.isBefore(start) || now.isAfter(end)) {
            log.info("[NO] is not event time = {}", now);

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(400);

            ResponseDto<Object> fail = ResponseDto.fail(
                    400,
                    HttpStatus.BAD_REQUEST,
                    "이벤트의 기간이 아닙니다. 이벤트 시간을 확인해주세요."
            );
            String responseDto = objectMapper.writeValueAsString(fail);
            response.getWriter().write(responseDto);

            return false;
        }
        log.info("[OK] is event time");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("========== END ==========");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
