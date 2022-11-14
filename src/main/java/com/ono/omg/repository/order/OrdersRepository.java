package com.ono.omg.repository.order;

import com.ono.omg.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {


}
