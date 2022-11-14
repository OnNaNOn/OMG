//package com.ono.omg.controller;
//
//import com.ono.omg.dto.common.ResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class AccountController {
//
////    private final AccountService accountService;
//
//    /**
//     * 회원가입
//     */
//    @PostMapping("/signup")
//    public ResponseDto<AccountAuthResponseDto> registerAccount(@RequestBody @Valid AccountSignupRequestDto accountRequestDto) {
//        return ResponseDto.success(accountService.signup(accountRequestDto));
//    }
//
//    /**
//     * 로그인
//     */
//    @PostMapping("/login")
//    public ResponseDto<AccountAuthResponseDto> login(@RequestBody AccountLoginRequestDto accountLoginRequestDto, HttpServletResponse response) {
//        return ResponseDto.success(accountService.login(accountLoginRequestDto, response));
//    }
//
//    /**
//     * 이메일 중복 확인
//     */
//    @PostMapping("/account/duplication/email")
//    public ResponseDto<String> dupEmail(@RequestBody AccountSignUpDuplicateEmailDto accountSignUpDuplicateEmailDto) {
//        return accountService.isExistEmail(accountSignUpDuplicateEmailDto);
//    }
//
//    /**
//     * 닉네임 중복 확인
//     */
//    @PostMapping("/account/duplication/username")
//    public ResponseDto<String> dupUsername(@RequestBody AccountSignUpDuplicateUsernameDto accountSignUpDuplicateUsernameDto) {
//        return accountService.isExistUsername(accountSignUpDuplicateUsernameDto);
//    }
//
//    @PatchMapping("/account/profile")
//    public ProfileResponseDto editProfile(@RequestParam(required = false, value = "file") MultipartFile multipartFile,
//                                                @RequestParam(value = "name") AccountSignUpDuplicateUsernameDto accountSignUpDuplicateUsernameDto,
//                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        return accountService.modifiedProfile(multipartFile, accountSignUpDuplicateUsernameDto.getUsername(), userDetails.getUsername());
//    }
//
////    //로그아웃
////    @PostMapping
////    public HttpHeaders setHeaders() {
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
////        return headers;
////    }
//}
