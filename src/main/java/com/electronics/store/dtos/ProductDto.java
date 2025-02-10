package com.electronics.store.dtos;

import com.electronics.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDto {

    private String productId;

    private String title;

    private String description;

    private int price;

    private int discountPrice;

    private int quantity;

    private Date addedDate;

    private boolean live ;

    private boolean stock;

    @ImageNameValid
    private String productImageName;

    private CategoryDto category;
}
