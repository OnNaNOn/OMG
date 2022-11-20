//package com.ono.omg.repository.product;
//
//import com.ono.omg.domain.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//import static com.ono.omg.dto.response.ProductResponseDto.*;
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//class ProductRepositoryTest {
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @BeforeEach
//    public void before() {
//        for (int i = 0; i < 15; i++) {
//            productRepository.save(new Product("피카츄" + i, 1000 + i, "포켓몬", "초고속 배송", 10 + i, 1L));
//        }
//    }
//
//    @Test
//    public void 관리자_재고_현황_페이지() throws Exception {
//        // given
//        PageRequest pageable1 = PageRequest.of(0, 10);
//        PageRequest pageable2 = PageRequest.of(1, 10);
//
//        // when
//        Page<AllProductManagementResponseDto> productStock1 = productRepository.findAllProductStock(pageable1);
//        Page<AllProductManagementResponseDto> productStock2 = productRepository.findAllProductStock(pageable2);
//
//        // then
////        assertThat(productStock1.getSize()).isEqualTo(10);
//    }
//
//}