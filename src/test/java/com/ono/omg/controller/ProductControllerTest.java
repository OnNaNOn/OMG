package com.ono.omg.controller;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.AccountRequestDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.ono.omg.dto.request.AccountRequestDto.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayName("ProductControllerTest 의")
@Transactional
class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Test
    public void get_products() throws Exception {
        Account account = accountRepository.save(new Account(new AccountRegisterRequestDto("jae", "pw", "pw")));
        productRepository.save(new Product("상품", 1000, "카테고리", "배송상태", 100, account.getId()));
        productRepository.save(new Product("상품", 1000, "카테고리", "배송상태", 100, account.getId()));
        productRepository.save(new Product("상품", 1000, "카테고리", "배송상태", 100, account.getId()));
        mockMvc.perform(
                        get("/api/omg?page=0")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(                                  // rest docs 문서 작성 시작
                        document("products-get") // 문서 조각 디렉토리 명
                );
    }

    @Test
    public void get_product_detail() throws Exception {
        Account account = accountRepository.save(new Account(new AccountRegisterRequestDto("jae", "pw", "pw")));
        Product product = productRepository.save(new Product("상품", 1000, "카테고리", "배송상태", 100, account.getId()));


        mockMvc.perform(
                        // 조회 API -> 대상의 데이터가 있어야 함
                        get("/api/products/detail/{productId}", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andDo( // rest docs 문서 작성 시작,
                        document("product-get", responseFields(
                                        fieldWithPath("productId").description("상품 번호"),
                                        fieldWithPath("imgUrl").description("상품 이미지"),
                                        fieldWithPath("title").description("상품명"),
                                        fieldWithPath("price").description("상품 가격"),
                                        fieldWithPath("category").description("카테고리"),
                                        fieldWithPath("delivery").description("배송 상태"),
                                        fieldWithPath("stock").description("상품 재고")
                                )
                        )
                );
    }
}