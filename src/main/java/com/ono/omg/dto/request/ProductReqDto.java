package com.ono.omg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductReqDto {

    private String title;
    private int price;
    private int stock;
    private String category;
    private String delivery;
}
