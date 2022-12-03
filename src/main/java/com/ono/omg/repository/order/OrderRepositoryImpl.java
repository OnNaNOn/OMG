package com.ono.omg.repository.order;

import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.dto.response.QOrderResponseDto_MainPageOrdersResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ono.omg.domain.QAccount.account;
import static com.ono.omg.domain.QOrder.order;
import static com.ono.omg.domain.QProduct.product;


@Slf4j
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<MainPageOrdersResponseDto> findOrdersParticularAccount(Pageable pageable, Long id) {
        List<MainPageOrdersResponseDto> results = queryFactory
                .select(new QOrderResponseDto_MainPageOrdersResponseDto(
                        product.id,
                        product.imgUrl,
                        product.title
                ))
                .from(order)
                .innerJoin(order.product, product)
                .innerJoin(order.account, account)
                .where(account.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return results;
    }
}
