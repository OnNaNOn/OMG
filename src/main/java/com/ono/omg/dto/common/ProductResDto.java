package com.ono.omg.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductResDto {

    private String title;
    private int price;
    private int stock;
    private String category;
    private String delivery;
    private int userid;
}
