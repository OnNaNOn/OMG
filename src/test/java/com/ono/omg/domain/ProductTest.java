package com.ono.omg.domain;

import com.ono.omg.dto.request.AccountRequestDto;
import com.ono.omg.dto.request.ProductReqDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("상품의")
class ProductTest {

    @Nested
    @DisplayName("Create 메서드는")
    class Create {
        @Test
        @DisplayName("상품에는 판매자 정보와 상품 관련 필드가 생성된다.")
        public void 상품_등록() throws Exception {
            // given
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);

            // when & then
            assertThat(product.getTitle()).isEqualTo("computer");
            assertThat(product.getImgUrl().equals("")).isFalse();
            assertThat(product.getCategory()).isEqualTo("컴퓨터");
            assertThat(product.getDelivery()).isEqualTo("초고속 배송");
            assertThat(product.getStock()).isEqualTo(100);
            assertThat(product.getSellerId()).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("Update 메서드는")
    class Update {

        @Test
        @DisplayName("상품을 입력한 데이터로 업데이트 한다.")
        public void 상품_수정() throws Exception {
            // given
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
            String basicImgUrl = "https://jaesa-bucket.s3.ap-northeast-2.amazonaws.com/omg.jpg";
            ProductReqDto updateProduct = new ProductReqDto("당근", 1000, 10, "채소", "빠른 배송", basicImgUrl);

            // when
            product.updateProduct(updateProduct);

            // then
            assertThat(product.getTitle()).isEqualTo("당근");
            assertThat(product.getImgUrl()).isEqualTo(basicImgUrl);
            assertThat(product.getCategory()).isEqualTo("채소");
            assertThat(product.getDelivery()).isEqualTo("빠른 배송");
            assertThat(product.getStock()).isEqualTo(10);
            assertThat(product.getSellerId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("상품이 판매되면 재고가 한 개씩 깎인다.")
        public void 상품_판매() throws Exception {
            // given
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);

            // when
            for (int i = 0; i < 10; i++) {
                product.decreaseStock(product.getStock());
            }

            // then
            assertThat(product.getStock()).isEqualTo(90);
        }
    }

    @Nested
    @DisplayName("Delete 메서드는")
    class Delete {

        @Test
        @DisplayName("등록된 상품을 삭제한다. 단, 값을 지우는 것이 아닌 YN 필드를 변경한다.")
        public void 상품_삭제() throws Exception {
            // given
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);

            // when
            product.isDeleted();

            // then
            assertThat(product.getIsDeleted()).isEqualTo("Y");
        }
    }






}