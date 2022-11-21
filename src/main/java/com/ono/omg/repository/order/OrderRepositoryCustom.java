package com.ono.omg.repository.order;

import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.CreatedOrdersResponseDto;

public interface OrderRepositoryCustom {

    List<CreatedOrdersResponseDto> findOrdersParticularAccount(Pageable pageable, Long id);
}
