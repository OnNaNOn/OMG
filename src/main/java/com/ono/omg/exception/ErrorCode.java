package com.ono.omg.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    DUPLICATE_EMAIL(400, HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다."),
    DUPLICATE_USERNAME(400, HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다."),
    NOT_EQUAL_PASSWORD(400, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    UNREGISTER_USER(400, HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),

    DUPLICATE_PRODUCT(400, HttpStatus.BAD_REQUEST, "중복된 상품이 존재합니다."),
    // 401 UNAUTHORIZED 권한 없음
    UNAUTHORIZED_USER(401, HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다."),

    // 403 FORBIDDEN 접근 실패
    FORBIDDEN_USER(403, HttpStatus.FORBIDDEN, "로그인이 필요합니다."),
    INVALID_TOKEN(403, HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다."),
    INVALID_USER(403, HttpStatus.FORBIDDEN, "올바른 사용자가 아닙니다."),
    OUT_OF_STOCK(403, HttpStatus.FORBIDDEN, "재고가 없습니다. 판매자에게 문의하세요"),

    // 404 NOT_FOUND 존재하지 않음
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    ORDER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 주문이 존재하지 않습니다."),
    NOT_FOUND_PRODUCT(404, HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
    EMAIL_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 이메일은 존재하지 않습니다."),
    NOT_FOUND_SELLER(404, HttpStatus.NOT_FOUND, "존재하지 않는 판매자입니다."),
    NOT_FOUND_EVENT(404, HttpStatus.NOT_FOUND, "해당 이벤트는 존재하지 않습니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    NO_CONSISTENCY_STOCK(500, HttpStatus.INTERNAL_SERVER_ERROR, "재고 수량이 마이너스(-)입니다. 확인이 필요합니다.");

    private final int status;
    private final HttpStatus httpStatus;
    private final String message;
}
