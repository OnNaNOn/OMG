package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.QProductResponseDto_AllProductManagementResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ono.omg.domain.QProduct.product;
import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<AllProductManagementResponseDto> findAllProductStock(Pageable pageable) {
        List<AllProductManagementResponseDto> results = queryFactory
                .select(new QProductResponseDto_AllProductManagementResponseDto(
                        product.id,
                        product.title,
                        product.price,
                        product.stock
                ))
                .from(product)
                .orderBy(product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);
        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchCount());
    }
}
