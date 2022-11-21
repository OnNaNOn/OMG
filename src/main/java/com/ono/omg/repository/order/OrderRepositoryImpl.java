package com.ono.omg.repository.order;

import com.ono.omg.dto.response.QOrderResponseDto_CreatedOrdersResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ono.omg.domain.QAccount.account;
import static com.ono.omg.domain.QOrder.order;
import static com.ono.omg.domain.QProduct.product;
import static com.ono.omg.dto.response.OrderResponseDto.CreatedOrdersResponseDto;

@Slf4j
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<CreatedOrdersResponseDto> findOrdersParticularAccount(Pageable pageable, Long id) {
        List<CreatedOrdersResponseDto> results = queryFactory
                .select(new QOrderResponseDto_CreatedOrdersResponseDto(
                        order.product.imgUrl,
                        order.product.title
                ))
                .from(order)
                .join(order.product, product)
                .join(order.account, account)
                .where(account.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return results;
    }
}
