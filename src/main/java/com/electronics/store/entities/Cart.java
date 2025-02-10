package com.electronics.store.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdAt;

    @OneToOne
    private User  user;

    // mapping karni ai cartItem wali
    // agar cart update hota hai to saare items bhi update hojayenge
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> items= new ArrayList<>();
}
