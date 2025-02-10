package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;

    private String title;

    @Column(length = 1000)
    private String description;

    private int price;

    private int discountPrice;

    private int quantity;

    private Date addedDate;

    private boolean live ;

    private boolean stock;

    private String productImageName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


}
