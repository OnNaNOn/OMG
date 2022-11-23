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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ono.omg.dto.response.OrderResponseDto.cancelOrderResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    private final RedissonClient redissonClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 주문하기
     */
    @Transactional
    public createdOrdersResponseDto productOrder(Long productId, Account account) {
        Product findProduct = validateProduct(productId);
        Account findAccount = validateAccount(account.getId());

        findProduct.decreaseStock(1);
        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = orderRepository.save(new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice())));

        return new createdOrdersResponseDto(
                savedOrder.getId(), savedOrder.getTotalPrice(), account.getUsername(), findProduct
        );
    }

    /**
     * 주문하기 (Redis - Redisson)
     */
    public createdOrdersResponseDto productOrderRedisson(Long productId, Account account) {
        RLock lock = redissonClient.getLock(productId.toString());

        createdOrdersResponseDto responseDto;
        try {
            // 몇 초동안 점유할 것인지에 대한 설정
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            // 점유하지 못한 경우
            if(!available) {
                System.out.println("lock 획득 실패");
                throw new RuntimeException("락 획득 실패");
            }

            // lock 획득 성공
            responseDto = createOrderWithRedisson(productId, account.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 락 해제
            lock.unlock();
        }
        return responseDto;
    }

    @Transactional
    protected createdOrdersResponseDto createOrderWithRedisson(Long productId, Long accountId) {
        Product findProduct = validateProduct(productId);
        Account findAccount = validateAccount(accountId);

        findProduct.decreaseStock(1);
        productRepository.save(findProduct);
        System.out.println("findProduct.getStock() = " + findProduct.getStock());
        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = orderRepository.save(new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice())));

        return new createdOrdersResponseDto(
                savedOrder.getId(), savedOrder.getTotalPrice(), findAccount.getUsername(), findProduct
        );
    }

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

    private Account validateAccount(Long accountId) {
        Account findAccount = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        return findAccount;
    }

    private Product validateProduct(Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );
        return findProduct;
    }

    /**
     * SJ: 지워도 괜찮은지요??
     */
    @Transactional
    public void testDecrease(Long productId, Account account) {
        Product findProduct = validateProduct(productId);

        Account findAccount = validateAccount(account.getId());

        /**
         * 아래 문장을 Order 생성자에서 실행되도록 변경
         */
//        findProduct.decrease();

        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice()));
//        productRepository.saveAndFlush(findProduct);
//        orderRepository.saveAndFlush(savedOrder);
    }

    /**
     * 주문에 상품이 여러개일 경우를 대비해 별도의 메서드로 분리
     */
    private Integer getTotalOrderPrice(int price) {
        return price;
    }

    public List<MainPageOrdersResponseDto> findAllOrders(Pageable pageable, Account account) {
        List<MainPageOrdersResponseDto> findOrders = orderRepository.findOrdersParticularAccount(pageable, account.getId());

        return findOrders;
    }

    @Transactional
    public cancelOrderResponseDto cancel(Long orderId, Account account) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.ORDER_NOT_FOUND)
        );

        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        if(isSameAccount(findOrder, findAccount)) {
            throw new CustomCommonException(ErrorCode.INVALID_USER);
        }

        /**
         * SJ:
         * 우리 서비스만의 [주문 취소] 정책이 필요함
         * 예를 들어, 주문 취소를 했을 경우 재고 관리에 창고재고 라는 필드를 만들어서
         * 해당 값은 증가 시키고 사용자에게 보여지는 상품 재고는 유지를 한다던지와 같은
         *
         * 더불어 재고 관리 페이지에 [주문 취소]와 [판매 유무] 버튼이 필요함
         */

        /**
         * 주문 취소로 변경
         */
        findOrder.orderCancel();

        Long productId = findOrder.getProduct().getId();
        String productName = findOrder.getProduct().getTitle();
        String orderStatus = findOrder.getOrderType().getStatus();

        return new cancelOrderResponseDto(productId, productName, orderStatus);


    }

    /**
     * 상품 검색
     * */
    @Transactional
    public Page<SearchResponseDto> searchOrders(SearchRequestDto requestDto, Pageable pageable) {
        return productRepository.searchProduct(requestDto, pageable);
    }


//    /**
//     * 주문내역 조회
//     * */
//    public List<MyPageResponseDto> getOrderList(UserDetailsImpl userDetails) {
//        Long accountId = userDetails.getAccount().getId();
//        accountRepository.findById(accountId).orElseThrow(
//                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
//        );
//
//        List<Order> orders = orderRepository.findorderListByOrderId(accountId);
//        List<MyPageResponseDto> responseDtoList = orders.stream()
//                .map((o)-> new MyPageResponseDto(o.getProduct().getImgUrl(), o.getProduct().getTitle()))
//                .collect(Collectors.toList());
//
//        return responseDtoList;
//    }


    private boolean isSameAccount(Order findOrder, Account findAccount) {
        return !findOrder.getAccount().getId().equals(findAccount.getId());
    }

}