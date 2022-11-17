package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.redisson.RedissonLockStockFacade;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ono.omg.dto.response.OrderResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    private RedissonClient redissonClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 주문하기
     * @param productId
     * @param account
     * @return
     */

    public CreatedOrdersResponseDto productOrder(Long productId, Account account) {
        RLock lock = redissonClient.getLock(productId.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
            }
            log.info("lock 획득");
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
                    findProduct.getSellerId());

            findProduct.decrease();
            productRepository.saveAndFlush(findProduct);

            return createdOrderDto;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            log.info("lock 반납");
        }
    }

    public void testDecrease(Long productId, Account account) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        Account findAccount = accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        findProduct.decrease();

        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice()));
        orderRepository.save(savedOrder);

        // 별도의 public method 로 만들고 controller 에서 호출하는 것이 바람직한지?
//        logger.info("u_id: "+ account.getId() + ", p_id: "+ productId);


//        CreatedOrdersResponseDto createdOrderDto = new CreatedOrdersResponseDto(savedOrder.getId(),
//                savedOrder.getTotalPrice(),
//                findAccount.getUsername(),
//                findProduct.getTitle(),
//                findProduct.getCategory(),
//                findProduct.getDelivery(),
//                findProduct.getSellerId());

        productRepository.saveAndFlush(findProduct);
    }

    private Integer getTotalOrderPrice(int price) {
        return price;
    }

    public List<CreatedOrdersResponseDto> findAllOrders(Account account) {
        List<CreatedOrdersResponseDto> findOrders = orderRepository.findOrdersParticularAccount(account.getId());

        return findOrders;
    }
}
