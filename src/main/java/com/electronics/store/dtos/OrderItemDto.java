package com.electronics.store.dtos;

import com.electronics.store.entities.Order;
import com.electronics.store.entities.Product;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {



    private int orderItemId;

    private int quantity;
    private int totalPrice;

    private ProductDto product;


}
