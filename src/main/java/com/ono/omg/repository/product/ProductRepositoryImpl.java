package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import com.ono.omg.domain.QProduct;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.QProductResponseDto_AllProductManagementResponseDto;
import com.ono.omg.dto.response.QSearchResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
//                .orderBy(product.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

//        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);
//        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Page<SearchResponseDto> searchProductUsedFullTextSearch(SearchRequestDto requestDto, Pageable pageable) {
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
                        titleMatch(requestDto.getTitle())
                )
//                .orderBy(product.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

//        JPAQuery<Product> countQuery = queryFactory.selectFrom(product);
//        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Page<SearchResponseDto> searchProductUsedFullTextSearchAndRowLookup(SearchRequestDto requestDto, Pageable pageable) {

        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(product.id)
                .from(product)
                .where(titleMatch(requestDto.getTitle()))
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
                .where(product.id.in(ids))
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
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
