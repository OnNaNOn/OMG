package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 주문하기
     *
     * @param productId
     * @param account
     * @return
     */
    public CreatedOrdersResponseDto productOrder(Long productId, Account account) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        Account findAccount = accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice()));

        // 별도의 public method 로 만들고 controller 에서 호출하는 것이 바람직한지?
        logger.info("u_id: "+ account.getId() + ", p_id: "+ productId);

        orderRepository.save(savedOrder);

        CreatedOrdersResponseDto createdOrderDto = new CreatedOrdersResponseDto(savedOrder.getId(),
                savedOrder.getTotalPrice(),
                findAccount.getUsername(),
                findProduct.getTitle(),
                findProduct.getCategory(),
                findProduct.getDelivery(),
                findProduct.getUserid());
        return createdOrderDto;
    }

    private Integer getTotalOrderPrice(int price) {
        return price;
    }

    public List<CreatedOrdersResponseDto> findAllOrders(Account account) {
        List<CreatedOrdersResponseDto> findOrders = orderRepository.findOrdersParticularAccount(account.getId());

        return findOrders;
    }
}
