package com.ono.omg.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("댓글 의")
class ReviewTest {

    @Nested
    @DisplayName("Create 메서드는")
    class Create {

        @Test
        @DisplayName("댓글을 생성한다.")
        public void 댓글_생성() throws Exception {
            // given
            Product givenProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);

            // when
            Review createReview = new Review(givenProduct, "댓글입니다.", 10L);

            // then
            assertThat(createReview.getReviewContent()).isEqualTo("댓글입니다.");
            assertThat(createReview.getProduct().getTitle()).isEqualTo(givenProduct.getTitle());
        }
    }

    @Nested
    @DisplayName("Update 메서드는")
    class Update {

        @Test
        @DisplayName("댓글 내용에 대해서만 수정한다. ")
        public void 댓글_수정() throws Exception {
            // given
            Product givenProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
            Review createReview = new Review(givenProduct, "댓글입니다.", 10L);

            // when
            createReview.setReviewContent("================");

            // then
            assertThat(createReview.getReviewContent()).isEqualTo("================");
        }

        @Nested
        @DisplayName("Delete 메서드는")
        class Delete {

            @Test
            @DisplayName("댓글을 삭제한다. 단, YN 필드만 Y로 변경한다.")
            public void 댓글_삭제() throws Exception {
                // given
                Product givenProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
                Review createReview = new Review(givenProduct, "댓글입니다.", 10L);

                // when
                createReview.deleteReview();

                // then
                assertThat(createReview.getIsDeleted().equals("Y")).isTrue();
            }

        }

    }
}