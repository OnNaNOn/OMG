package com.ono.omg.controller.view;

import com.ono.omg.domain.Product;
import com.ono.omg.domain.Review;
import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;
import com.ono.omg.dto.response.ProductResponseDto.detailProductResponseDto;
import com.ono.omg.dto.response.ReviewResponseDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.RegistedProductResponseDto;

@Controller
@RequiredArgsConstructor
public class UIController {
    /**
     * SJ: 계층형 아키텍처에 맞게 재고관리와 메인 페이지는
     *  Controller > Service > Repository로 변경할 필요 있음 
     */
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/accounts/signup")
    public String register() {
        return "accounts/accountRegisterForm";
    }

    @GetMapping("/accounts/login")
    public String accountsLoginForm() {
        return "accounts/accountLoginForm";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "accounts/adminLoginForm";
    }

    //메인페이지
    @GetMapping("/omg")
    public String mainPage(@PageableDefault(size = 10) Pageable pageable, Model model){
        Page<Product> products = productRepository.findAll(pageable);
        List<MainPageResponseDto> responseDto = new ArrayList<>();

//        for(Product product : products){
//            responseDto.add(new MainPageResponseDto(product));
//        }
//
//        model.addAttribute("products", responseDto);

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = products.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage+2, products.getTotalPages());

        model.addAttribute("products", products);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("max", products.getTotalPages());
        model.addAttribute("productsSize", products.getTotalElements());

        return "main/mainPage";
    }

    /**
     * 관리자 재고 관리 페이지
     */
    @GetMapping("/admin/management")
    public String managedPage(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<ProductResponseDto.AllProductManagementResponseDto> productStock = productRepository.findAllProductStock(pageable);

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = productStock.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage+2, productStock.getTotalPages());

        model.addAttribute("products", productStock);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("max", productStock.getTotalPages());
        model.addAttribute("productsSize", productStock.getTotalElements());

        return "admin/managedProducts";
    }

    //상세페이지
    @GetMapping("/products/detail/{productId}")
    public String detailProductPage(Model model, @PathVariable Long productId){
        Product product = productRepository.detailProduct(productId);
        detailProductResponseDto responseDto = new detailProductResponseDto(product);

//////////////////////////////////////////////////////////////////////////////////////////////

        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        List<ReviewResponseDto> responseDto1 = new ArrayList<>();

        for (Review review : reviews) {
            String username = accountRepository.findUsernameByAccountId(review.getId());
            responseDto1.add(new ReviewResponseDto(review.getId(), review.getProduct().getId(), username, review.getReviewContent(), review.getCreatedAt() ,review.getModifiedAt()));
        }

        model.addAttribute("product", responseDto);
        model.addAttribute("reviews", responseDto1);

        return "product/detailProduct";
    }

    // 마이 페이지
    @GetMapping("/accounts/mypage")
    public String accountMyPage(Model model) {
        List<Product> products = productRepository.findAll();
        List<RegistedProductResponseDto> responseDto = new ArrayList<>();

        for (Product product : products) {
            responseDto.add(new RegistedProductResponseDto(product));
        }

        model.addAttribute("products", responseDto);

        return "mypage/accountPrivatePage";
    }

//    //리뷰 조회
//    @GetMapping("/api/products/detail/{productId}")
//    public String getReviewList(Model model){
//        List<Review> reviews = reviewRepository.findAll();
//        List<ReviewResponseDto> responseDto = new ArrayList<>();
//
//        for (Review review : reviews) {
//            String username = accountRepository.findUsernameByAccountId(review.getId());
//
//            responseDto.add(new ReviewResponseDto(review.getProduct().getId(), username, review.getReviewContent(), review.getCreatedAt() ,review.getModifiedAt()));
//        }
//        model.addAttribute("reviews", responseDto);
//
//        return "product/detailProduct";
//    }
}

