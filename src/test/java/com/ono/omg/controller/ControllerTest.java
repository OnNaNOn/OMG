package com.ono.omg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.jwt.JwtUtil;
import com.ono.omg.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.ono.omg.dto.request.AccountRequestDto.AccountLoginRequestDto;
import static com.ono.omg.dto.response.AccountResponseDto.AccountLoginResponseDto;
import static org.mockito.BDDMockito.given;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest({
        AccountController.class
//        ,
//        AdminController.class,
//        CartController.class,
//        DetailController.class,
//        LikesController.class,
//        MainPageController.class,
//        MyPageController.class,
//        OrderController.class,
//        ProductController.class,
//        ReviewController.class
})
@ActiveProfiles("test")
@EnableJpaAuditing
public abstract class ControllerTest {
    // SAVE, UPDATE, FIND, REMOVE

    protected static String VALID_TOKEN;


    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtUtil jwtUtil;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected AccountRepository accountRepository;

    @MockBean
    protected ProductRepository productRepository;


//    @BeforeEach
//    public void setInit(WebApplicationContext context) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//
//        AccountLoginResponseDto responseDto = new AccountLoginResponseDto();
//
//        given(accountService.login(new AccountLoginRequestDto("jae", "pw"),
//                new MockHttpServletResponse())).
//                willReturn(responseDto);
//
//        System.out.println("responseDto = " + responseDto);

//        TokenDto token = jwtUtil.createAllToken("jae");
//        VALID_TOKEN = token.getAccessToken();
//    }
//
//    protected ResultActions requestGet(final String url) throws Exception {
//        return mockMvc.perform(get(url)
//                        .header(HttpHeaders.AUTHORIZATION, VALID_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//    }

}
