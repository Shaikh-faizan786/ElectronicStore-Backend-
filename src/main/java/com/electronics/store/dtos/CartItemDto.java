package com.electronics.store.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private int cartItemId;

    private ProductDto product;

    private int quantity;

    private int totalPrice;



}
