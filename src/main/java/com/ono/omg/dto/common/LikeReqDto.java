package com.ono.omg.dto.common;


import com.ono.omg.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LikeReqDto {

    private int product_id;
    private int user_id;
}
