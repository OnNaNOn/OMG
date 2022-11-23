package com.ono.omg.repository.order;

import com.ono.omg.dto.response.OrderResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface OrderRepositoryCustom {

    List<MainPageOrdersResponseDto> findOrdersParticularAccount(Pageable pageable, Long accountId);
}
