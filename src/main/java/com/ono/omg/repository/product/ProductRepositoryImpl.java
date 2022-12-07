package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.QProductResponseDto_AllProductManagementResponseDto;
import com.ono.omg.dto.response.QSearchResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ono.omg.domain.QProduct.product;
import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * findAllProductStock:: 성능 개선 없이 단순 QueryDSL 만 사용
     * ㄴ 현재 관리자 페이지에 쓰이고 있음
     */
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
                .where(product.isSale.eq("Y"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 상품검색
     * ㄴ OrderService.searchOrders()에 쓰이고 있음
     */
    @Override
    public Page<SearchResponseDto> searchProduct(SearchRequestDto requestDto, Pageable pageable) {
        List<SearchResponseDto> results = queryFactory
                .select(new QSearchResponseDto(
                        product.id,
                        product.title,
                        product.price,
                        product.stock,
                        product.category,
                        product.delivery
                        )
                )
                .from(product)
                .where(titleEq(requestDto.getTitle()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int size = queryFactory.selectFrom(product).where(titleEq(requestDto.getTitle())).fetch().size();

        return new PageImpl<>(results, pageable, size);
//        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<SearchResponseDto> searchProductUsedFullTextSearch(SearchRequestDto requestDto, Pageable pageable) {
        List<SearchResponseDto> results = queryFactory
                .select(new QSearchResponseDto(
                                product.id,
                                product.title,
                                product.price,
                                product.stock,
                                product.category,
                                product.delivery
                        )
                )
                .from(product)
                .where(titleMatch(requestDto.getTitle()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
//        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
    }


    @Override
    public Page<SearchResponseDto> searchProductUsedFullTextSearchAndCoveringIndex(String title, Pageable pageable) {

        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(product.id)
                .from(product)
                .where(titleMatch(title))
                .orderBy(product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        // 2) 1의 결과로 발생한 id로 실제 select절 조회
        List<SearchResponseDto> results = queryFactory
                .select(new QSearchResponseDto(
                                product.id,
                                product.title,
                                product.price,
                                product.stock,
                                product.category,
                                product.delivery
                        )
                )
                .from(product)
                .where(product.id.in(ids))
                .fetch();

        int size = queryFactory
                .select(product.id)
                .from(product)
                .where(titleMatch(title))
                .fetch().size();

        if(size % 10 > 0) {
            size++;
        }
        return new PageImpl<>(results, pageable, size);
    }

    private BooleanExpression titleMatch(String title) {
        if(!StringUtils.hasText(title)) {
            return null;
        }
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", product.title, "+" + title + "*");

        return booleanTemplate.gt(0);
    }

    private BooleanExpression titleEq(String productTitle) {
        return StringUtils.hasText(productTitle) ? product.title.eq(productTitle) : null;
    }
}
