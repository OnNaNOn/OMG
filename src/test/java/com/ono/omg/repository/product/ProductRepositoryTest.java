package com.ono.omg.repository.product;

import com.ono.omg.common.RepositoryTest;
import com.ono.omg.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ProductRepository 의")
class ProductRepositoryTest extends RepositoryTest {

    @Autowired
    ProductRepository productRepository;

//    @Test
//    @DisplayName("findAllProductStock 는 재고가 있는 모든 상품에 대해 조회한다.")
//    public void findAllProductStock() throws Exception {
//        // given
//        PageRequest pageable = PageRequest.of(1, 10);
//        Page<AllProductManagementResponseDto> hasStockProducts = findAllProductHasStock();
//
//        // when
////        Page<AllProductManagementResponseDto> products1 = productRepository.findAllProductStock(pageable);
//        System.out.println("products1 = " + hasStockProducts.getContent());
//
//        productRepository.findById(hasStockProducts.getContent().get(0).getProductId()).ifPresent(
//                product -> product.decreaseStock(product.getStock())
//        );
//        Page<AllProductManagementResponseDto> products2 = productRepository.findAllProductStock(pageable);
//
//
//        // then
////        assertThat(products1.getSize()).isEqualTo(HasStockProductCount);
////        assertThat(products1.getSize()).isEqualTo(HasStockProductCount - 1);
//
//
//    }

}
