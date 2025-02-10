package com.electronics.store.dtos;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDto {

    private String orderId;
    private String orderStatus="PENDING";
    private  String paymentStatus="NOTPAID";
    private int ordeAmount;
    private String billingPhone;
    private String billingName;
    private String billingAddress;
    private Date orderDate= new Date();
    private Date deliveredDate;

//    private UserDto userDto;

    private List<OrderItemDto> orderItem = new ArrayList<>();
}
