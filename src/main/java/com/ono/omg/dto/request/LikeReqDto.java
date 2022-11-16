package com.ono.omg.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LikeReqDto {

    private int product_id;
    private int user_id;
}
