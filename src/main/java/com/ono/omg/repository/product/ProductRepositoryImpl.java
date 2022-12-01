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
    public Page<SearchResponseDto> searchProductUsedFullTextSearchAndCoveringIndex(SearchRequestDto requestDto, Pageable pageable) {

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
                .selectFrom(product)
                .where(product.id.in(ids))
                .fetch().size();

        return new PageImpl<>(results, pageable, size);
    }

//    @Override
//    public Page<SearchResponseDto> searchProductUsedFullTextSearchAndCoveringIndex(SearchRequestDto requestDto, Pageable pageable, String useSearch) {
//
//        // 1) 커버링 인덱스로 대상 조회
//        List<Long> ids = queryFactory
//                .select(product.id)
//                .from(product)
//                .where(titleMatch(requestDto.getTitle()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
//        if (CollectionUtils.isEmpty(ids)) {
//            return new PageImpl<>(new ArrayList<>(), pageable, 0);
//        }
//
//        // 2) 1의 결과로 발생한 id로 실제 select절 조회
//        JPAQuery<SearchResponseDto> results = queryFactory
//                .select(new QSearchResponseDto(
//                                product.id,
//                                product.title,
//                                product.price,
//                                product.stock,
//                                product.category,
//                                product.delivery
//                        )
//                )
//                .from(product)
//                .where(product.id.in(ids));
//
//        if(useSearch.equals("true")) {
//            int fixedPageCount = pageable.getPageSize() * 10;
//            return new PageImpl<>(results.fetch(), pageable, fixedPageCount);
//        }
//
//        long totalCount = results.fetchCount();
//        Pageable pageRequest = exchangePageRequest(pageable, totalCount); // 데이터 건수를 초과한 페이지 버튼 클릭시 보정
//        return new PageImpl<>(querydsl().applyPagination(pageRequest, results).fetch(), pageRequest, totalCount);
//    }

    Pageable exchangePageRequest(Pageable pageable, long totalCount) {

        /**
         *  요청한 페이지 번호가 기존 데이터 사이즈를 초과할 경우
         *  마지막 페이지의 데이터를 반환한다
         */
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long requestCount = (pageNo - 1) * pageSize; // pageNo:10, pageSize:10 일 경우 requestCount=90

        if (totalCount > requestCount) { // 실제 전체 건수가 더 많은 경우엔 그대로 반환
            return pageable;
        }

        int requestPageNo = (int) Math.ceil((double)totalCount/pageNo); // ex: 71~79이면 8이 되기 위해
        return PageRequest.of(requestPageNo, pageSize);

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
