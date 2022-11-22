package com.ono.omg.repository.order;

import com.ono.omg.common.RepositoryTest;
import com.ono.omg.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderRepository 의")
class OrderRepositoryTest extends RepositoryTest {

    @Autowired
    OrderRepository orderRepository;

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