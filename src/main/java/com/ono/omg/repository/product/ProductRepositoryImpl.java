package com.ono.omg.repository.product;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductInfoResponseDto;

public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<AllProductInfoResponseDto> findAllProducts() {
//        queryFactory
//                .select(new Q(
//
//                ))
//            .from(Product)

        return null;
    }
}