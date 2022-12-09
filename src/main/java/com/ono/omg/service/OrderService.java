package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.createdOrdersResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

//    private final RedissonClient redissonClient;

    /**
     * 주문하기 (@Lock - 비관적 락)
     */
    @Transactional
    public createdOrdersResponseDto productOrderWithPessimisticLock(Long productId, Long accountId) {
        Account findAccount = validateAccount(accountId);
//        Product findProduct2 = validateProduct(productId);
        Product findProduct = productRepository.findByIdWithPessimisticLock(productId);


        findProduct.decreaseStock(1);
//        productRepository.saveAndFlush(findProduct);

        Order savedOrder = orderRepository.save(new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice())));
        return new createdOrdersResponseDto(savedOrder.getId(), savedOrder.getTotalPrice(), findAccount.getUsername(), findProduct);
    }

    @Transactional(readOnly = true)
    public Page<SearchResponseDto> searchOrders(SearchRequestDto requestDto, Pageable pageable) {
        return productRepository.searchProduct(requestDto, pageable);
    }

    private Account validateAccount(Long accountId) {
        Account findAccount = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        return findAccount;
    }

    public List<MainPageOrdersResponseDto> findAllOrders(Pageable pageable, Account account) {
        List<MainPageOrdersResponseDto> findOrders = orderRepository.findOrdersParticularAccount(pageable, account.getId());

        return findOrders;
    }

    /**
     * 주문에 상품이 여러개일 경우를 대비해 별도의 메서드로 분리
     */
    private Integer getTotalOrderPrice(int price) {
        return price;
    }

    /**
     * 상품 검색
     * */


//    /**
//     * 주문하기 (Redis - Redisson)
//     */
//    public createdOrdersResponseDto productOrderRedisson(Long productId, Account account) {
//        RLock lock = redissonClient.getLock(productId.toString());
//
//        createdOrdersResponseDto responseDto;
//        try {
//            // 몇 초동안 점유할 것인지에 대한 설정
//            boolean available = lock.tryLock(30, 10, TimeUnit.SECONDS);
//
//            // 점유하지 못한 경우 >> 100 개 요청 >> 41개
//            // 942 >> 45개
//            if(!available) {
//                System.out.println("lock 획득 실패");
//                throw new RuntimeException("락 획득 실패");
//            }
//
//            // lock 획득 성공
//            responseDto = createOrderWithRedisson(productId, account.getId());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            // 락 해제
//            lock.unlock();
//        }
//        return responseDto;
//    }
//
//    @Transactional
//    protected createdOrdersResponseDto createOrderWithRedisson(Long productId, Long accountId) {
//        Product findProduct = validateProduct(productId);
//        Account findAccount = validateAccount(accountId);
//
//        findProduct.decreaseStock(1);
//        productRepository.save(findProduct);
//        System.out.println("findProduct.getStock() = " + findProduct.getStock());
//        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
//        Order savedOrder = orderRepository.save(new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice())));
//
//        return new createdOrdersResponseDto(
//                savedOrder.getId(), savedOrder.getTotalPrice(), findAccount.getUsername(), findProduct
//        );
//    }
//
//    private Product validateProduct(Long productId) {
//        Product findProduct = productRepository.findById(productId).orElseThrow(
//                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
//        );
//        return findProduct;
//    }
}