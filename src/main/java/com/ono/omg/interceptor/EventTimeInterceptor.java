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

@Slf4j
public class EventTimeInterceptor implements HandlerInterceptor {

    private final EventRepository eventRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public EventTimeInterceptor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("========== START ==========");
        String requestURI = request.getRequestURI();
        log.info(requestURI);

        if (!requestURI.contains("confirm")) {
            return true;
        }

        Long productId = Long.valueOf(requestURI.split("/")[3]);
        log.info("productId = {}", productId);

        Event event = eventRepository.findByProductId(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_EVENT)
        );

        String start = event.getStartedAt().toString().substring(0, 13);
        String now = LocalDateTime.now().toString().substring(0, 13);

        System.out.println("start = " + start);
        System.out.println("now = " + now);

        if (now.compareTo(start) != 1) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(400);

            ResponseDto<Object> fail = ResponseDto.fail(
                    400,
                    HttpStatus.BAD_REQUEST,
                    "이벤트 시작일은 " + start.replace('T', ' ') + "시입니다."
            );
            String responseDto = objectMapper.writeValueAsString(fail);
            response.getWriter().write(responseDto);

            log.info("XXXX Event time is FALSE XXXX");

            return false;
        }
        log.info("OOOO Event time is TRUE OOOO");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("========== END ==========");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
