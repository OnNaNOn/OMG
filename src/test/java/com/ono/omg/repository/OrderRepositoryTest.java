package com.ono.omg.repository;

import com.ono.omg.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")

@DisplayName("OrderRepository 의")
class OrderRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findOrdersParticularAccount 는 특정 주문에 대해 조회를 했을 때 원하는 개수(1개)가 나와야 한다.")
    public void findOrdersParticularAccount() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.ofSize(1);
        Order order = saveOrders();
        System.out.println("order = " + order.getId());

        // when
        List<MainPageOrdersResponseDto> orders = orderRepository.findOrdersParticularAccount(pageRequest, order.getAccount().getId());

        // then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getTitle()).isEqualTo(order.getProduct().getTitle());
    }
}