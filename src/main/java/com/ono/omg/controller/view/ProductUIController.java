package com.ono.omg.controller.view;

import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.ono.omg.dto.response.ProductResponseDto.*;

@Controller
@Slf4j
public class ProductUIController {


    private ProductRepository productRepository;

    //SW : 사용하지않는 코드는 제거요망
    private AccountRepository accountRepository;

    public ProductUIController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 관리자 재고 관리 페이지
     */
    @GetMapping("/admin/management")
    public String managedPage(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<AllProductManagementResponseDto> productStock = productRepository.findAllProductStock(pageable);

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
}
