package com.electronics.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart id is required !!")
    private String cartId;
    @NotBlank(message = "User id is required !!")
    private String userId;

    private String orderStatus="PENDING";

    private  String paymentStatus="NOTPAID";
    @NotBlank(message = "Billing phone number is  required !!")
    private String billingPhone;
    @NotBlank(message = "Billing name  is required !!")
    private String billingName;
    @NotBlank(message = "Billing address  is required !!")
    private String billingAddress;

}
