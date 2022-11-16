package com.ono.omg.repository.order;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.CreatedOrdersResponseDto;

public interface OrderRepositoryCustom {

    List<CreatedOrdersResponseDto> findOrdersParticularAccount(Long id);
}
