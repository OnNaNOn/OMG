package com.ono.omg.repository;

import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.SearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductRepository 의")
class ProductRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllProductStock 는 재고가 있는 모든 상품에 대해 조회한다.")
    public void findAllProductStock() throws Exception {
        // given
        findAllProductHasStock();

        PageRequest pageable = PageRequest.ofSize(5);
        Page<AllProductManagementResponseDto> products1 = productRepository.findAllProductStock(pageable);
        AllProductManagementResponseDto findIndexZeroProduct = products1.getContent().get(0);

        // when
        productRepository.findById(findIndexZeroProduct.getProductId()).ifPresent(
                product -> {
                    product.decreaseStock(product.getStock());
                    productRepository.save(product);
                }
        );
        Page<AllProductManagementResponseDto> products2 = productRepository.findAllProductStock(pageable);

        // then
        assertThat(products1.getTotalElements()).isEqualTo(3);
        assertThat(products2.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("searchProduct 메서드는 상품명에 대해 검색하는데 일치하는 값이 존재한다.")
    public void searchByProductName() throws Exception {
        // given
        findAllProductHasStock();

        PageRequest pageable = PageRequest.ofSize(5);
        SearchRequestDto givenProductName = new SearchRequestDto("항해1");

        // when
        Page<SearchResponseDto> searchProducts = productRepository.searchProduct(givenProductName, pageable);

        // then
        assertThat(searchProducts.getNumberOfElements()).isEqualTo(1);
        assertThat(searchProducts.getContent().get(0).getTitle()).isEqualTo("항해1");
    }

    @Test
    @DisplayName("searchByProductNameButNull 메서드는 상품명에 대해 검색한다. 단, 일치하는 값은 없다.")
    public void searchByProductNameButNull() throws Exception {
        // given
        findAllProductHasStock();

        PageRequest pageable = PageRequest.ofSize(5);
        SearchRequestDto givenProductName = new SearchRequestDto("항해");

        // when
        Page<SearchResponseDto> searchProducts = productRepository.searchProduct(givenProductName, pageable);

        // then
        assertThat(searchProducts.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("searchProductRequestDtoIsNull 메서드는 모든 입력값에 대해 NULL 을 입력했을 때에 대해 검색한다.")
    public void searchProductRequestDtoIsNull() throws Exception {
        // given
        findAllProductHasStock();

        PageRequest pageable = PageRequest.ofSize(5);
        SearchRequestDto givenProductName = new SearchRequestDto();

        // when
        Page<SearchResponseDto> searchProducts = productRepository.searchProduct(givenProductName, pageable);

        // then
        assertThat(searchProducts.getNumberOfElements()).isEqualTo(3);
    }
}
