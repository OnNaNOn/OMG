package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
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
    private Long userid;


    public ProductResDto(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getCategory();
        this.delivery = product.getDelivery();
        this.userid = product.getUserid();
    }
}
