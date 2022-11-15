package com.ono.omg.repository.order;

import com.ono.omg.dto.response.OrderResponseDto;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.*;

public interface OrderRepositoryCustom {

    List<CreatedOrdersResponseDto> findOrdersParticularAccount(Long id);
}
