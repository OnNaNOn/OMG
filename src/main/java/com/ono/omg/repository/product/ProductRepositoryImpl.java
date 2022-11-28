package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import com.ono.omg.domain.QProduct;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.QProductResponseDto_AllProductManagementResponseDto;
import com.ono.omg.dto.response.QSearchResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

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
                        product.stock,
                        product.isSale
                ))
                .from(product)
//                .where(product.isSale.eq("Y"))
//                .orderBy(product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);
        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 상품검색
     * */
    @Override
    public Page<SearchResponseDto> searchProduct(SearchRequestDto requestDto, Pageable pageable) {
        List<SearchResponseDto> results = queryFactory
                .select(new QSearchResponseDto(
                        product.title,
                        product.price,
                        product.stock,
                        product.category,
                        product.delivery,
                        product.sellerId,
                        product.isDeleted,
                        product.imgUrl
                        )
                )
                .from(product)
                .where(
                        titleEq(requestDto.getTitle())
                )
                .orderBy(product.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);
        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
    }
    private BooleanExpression titleEq(String productTitle) {
        return StringUtils.hasText(productTitle) ? product.title.eq(productTitle) : null;
    }


}
