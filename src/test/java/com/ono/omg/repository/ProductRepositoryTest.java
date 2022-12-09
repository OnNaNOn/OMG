package com.ono.omg.repository;

import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DisplayName("ProductRepository 의")
class ProductRepositoryTest extends RepositoryTest {

    @Autowired
    JPAQueryFactory queryFactory;

////    @Test
//    @DisplayName("findAllProductStock 는 재고가 있는 모든 상품에 대해 조회한다.")
//    public void findAllProductStock() throws Exception {
//        // given
//        productRepository.deleteAll();
//        findAllProductHasStock();
//
//        PageRequest pageable = PageRequest.ofSize(5);
//        Page<AllProductManagementResponseDto> products1 = productRepository.findAllProductStock(pageable);
//        AllProductManagementResponseDto findIndexZeroProduct = products1.getContent().get(0);
//
//        // when
//        productRepository.findById(findIndexZeroProduct.getProductId()).ifPresent(
//                product -> {
//                    product.decreaseStock(product.getStock());
//                    productRepository.save(product);
//                }
//        );
//        Page<AllProductManagementResponseDto> products2 = productRepository.findAllProductStock(pageable);
//
//        // then
//        assertThat(products1.getTotalElements()).isEqualTo(3);
//        assertThat(products2.getTotalElements()).isEqualTo(2);
//    }

////    @Test
//    @DisplayName("searchByProductNameButNull 메서드는 상품명에 대해 검색한다. 단, 일치하는 값은 없다.")
//    public void searchByProductNameButNull() throws Exception {
//        // given
//        productRepository.deleteAll();
//        findAllProductHasStock();
//
//        PageRequest pageable = PageRequest.ofSize(5);
//        SearchRequestDto givenProductName = new SearchRequestDto("항해");
//
//        // when
//        Page<SearchResponseDto> searchProducts = productRepository.searchProduct(givenProductName, pageable);
//
//        // then
//        assertThat(searchProducts.getNumberOfElements()).isEqualTo(0);
//    }
//
////    @Test
//    @DisplayName("searchProductRequestDtoIsNull 메서드는 모든 입력값에 대해 NULL 을 입력했을 때에 대해 검색한다.")
//    public void searchProductRequestDtoIsNull() throws Exception {
//        // given
//        productRepository.deleteAll();
//        findAllProductHasStock();
//
//        String keyword = null;
//
//        PageRequest pageable = PageRequest.ofSize(5);
//        SearchRequestDto givenProductName = new SearchRequestDto(keyword);
//
//        // when
//        Page<SearchResponseDto> searchProducts = productRepository.searchProduct(givenProductName, pageable);
//
//        // then
//        assertThat(searchProducts.getNumberOfElements()).isEqualTo(3);
//    }

    @Test
    @DisplayName("1m 24sec :: searchProduct 메서드는 상품명에 대해 검색하는데 일치하는 값이 존재한다. 단순 QueryDSL-JPA만 사용")
    public void searchByProductName() throws Exception {
        long start = System.currentTimeMillis();
        // given
        // findAllProductHasStock();

        String keyword = "스크";
        PageRequest pageable = PageRequest.of(3017, 10);
        SearchRequestDto givenProductName = new SearchRequestDto(keyword);

        // when
        Page<SearchResponseDto> results = productRepository.searchProduct(givenProductName, pageable);

        // then
//        assertThat(searchProducts.getNumberOfElements()).isEqualTo(1);
//        assertThat(searchProducts.getContent().get(0).getTitle()).isEqualTo("스크");

        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("소요시간:"+(System.currentTimeMillis()-start)+"ms");
    }

    @Test
    @DisplayName("17724ms :: searchMySQLFullTextSearchWithMatch 메서드는 '스크'라는 키워드를 기준으로 검색한다.")
    public void searchMySQLFullTextSearchWithMatch() throws Exception {
        // given
        String keyword = "스크"; // 스크의 검색 대상은 마 '스크', 아이 '스크' 림, 데 '스크' 탑
        productRepository.save(createProduct(keyword, 100));

        SearchRequestDto searchRequestDto = new SearchRequestDto(keyword);
        PageRequest pageable = PageRequest.of(4002, 10);

        Page<SearchResponseDto> results = productRepository.searchProductUsedFullTextSearch(searchRequestDto, pageable);

        System.out.println("totalElements = " + results.getTotalElements());
    }

    @Test
    @DisplayName("21749ms :: searchMySQLFullTextSearchWithMatchAndCoveringIndex 메서드는 '스크'라는 키워드를 기준으로 검색한다. 커버링 인덱싱 도입")
    public void searchMySQLFullTextSearchWithMatchAndCoveringIndex() throws Exception {
        long start = System.currentTimeMillis();
        // given
        String keyword = "스크"; // 스크의 검색 대상은 마 '스크', 아이 '스크' 림, 데 '스크' 탑
//        productRepository.save(createProduct(keyword, 100));

        SearchRequestDto searchRequestDto = new SearchRequestDto(keyword);
        PageRequest pageable = PageRequest.of(4002, 10);

        Page<SearchResponseDto> results = productRepository.searchProductUsedFullTextSearchAndCoveringIndex(keyword, pageable);

        System.out.println("totalElements = " + results.getTotalElements());
        // then
        System.out.println("소요시간:"+(System.currentTimeMillis()-start)+"ms");
    }

    @Test
    @DisplayName("951ms:: searchProductUsedFullTextSearchAndNoOffset")
    public void searchProductUsedFullTextSearchAndNoOffset() throws Exception {
        long start = System.currentTimeMillis();
        // given
        String keyword = "스크"; // 스크의 검색 대상은 마 '스크', 아이 '스크' 림, 데 '스크' 탑

        // when
        List<SearchResponseDto> results = productRepository.searchProductUsedFullTextSearchAndNoOffset(2035L, keyword, 10);

        // then
        System.out.println("소요시간:"+(System.currentTimeMillis()-start)+"ms");
    }
}
