package com.ono.omg.repository.order;

import com.ono.omg.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {


}
