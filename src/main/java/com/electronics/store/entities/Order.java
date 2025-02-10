package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Table(name = "orders")
public class Order {

    @Id
    private String  orderId;

    // PENDING,DISPATCHED,DELIVERED
    // ENUM
    private String orderStatus;

    //NOT-PAID,PAID
    // ENUM
    // BOOLEAN=>FALSE -> NOT PAID , BOOLEAN=>TRUE ->PAID

    private int ordeAmount;
    private  String paymentStatus;

    private String billingPhone;
    private String billingName;

    @Column(length = 1000)
    private String billingAddress;

    private Date orderDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,fetch =FetchType.EAGER )
    private List<OrderItem> orderItem = new ArrayList<>();

}
