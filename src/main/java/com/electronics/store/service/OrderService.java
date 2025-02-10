package com.electronics.store.service;

import com.electronics.store.dtos.CreateOrderRequest;
import com.electronics.store.dtos.OrderDto;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.entities.Cart;
import com.electronics.store.entities.Order;

import java.util.List;

public interface OrderService {

    // create order
    OrderDto createOrder(CreateOrderRequest orderDto);


    // remove order
    void removeOrder(String orderId);


    // get order of user
    List<OrderDto> getOrderOfUser(String userId);


    // get order
    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);

    // update Order
    OrderDto updateOrder(String orderId,OrderDto orderDto );

}
