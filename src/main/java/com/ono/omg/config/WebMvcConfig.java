package com.ono.omg.config;

import com.ono.omg.interceptor.EventTimeInterceptor;
import com.ono.omg.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final EventRepository eventRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EventTimeInterceptor(eventRepository));
    }
}
