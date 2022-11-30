package com.ono.omg.controller;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.AccountType;
import com.ono.omg.dto.response.AdminResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    /**
     * 관리자 재고 관리 페이지
     */
    @GetMapping("/api/admin/management")
    public AdminResponseDto managedPage(@RequestParam(name = "page") Integer page,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("admin page login = " + userDetails.getAccount().getUsername());

        validAccount(userDetails);

        PageRequest pageable = PageRequest.of(page, 10);
        Page<AllProductManagementResponseDto> productStock = productRepository.findAllProductStock(pageable);

        int totalPages = productStock.getTotalPages();
        long totalElements = productStock.getTotalElements();

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = productStock.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage = Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage + 2, totalPages);

        return new AdminResponseDto(productStock, totalPages, totalElements, nowPage, startPage, endPage);
    }

    private Account validAccount(UserDetailsImpl userDetails) {
        Account findAccount = accountRepository.findById(userDetails.getAccount().getId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        hasAdminRoleAccount(findAccount);

        return findAccount;
    }

    private void hasAdminRoleAccount(Account findAccount) {
        if (!findAccount.getAccountType().equals(AccountType.ROLE_ADMIN)) {
            throw new CustomCommonException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
