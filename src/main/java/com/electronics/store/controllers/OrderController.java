package com.electronics.store.controllers;
import com.electronics.store.dtos.ApiResponseMessage;
import com.electronics.store.dtos.CreateOrderRequest;
import com.electronics.store.dtos.OrderDto;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // create Order
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody  CreateOrderRequest orderDto){
        OrderDto order = orderService.createOrder(orderDto);
        return new ResponseEntity(order, HttpStatus.CREATED);
    }

    // remove order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable  String  orderId){
       orderService.removeOrder(orderId);
       ApiResponseMessage response =new  ApiResponseMessage();
       response.setMessage("Order Is Deleted");
       response.setStatus(HttpStatus.OK);
        response.setSuccess(true);
       return new ResponseEntity(response, HttpStatus.OK);
    }

    // get order of the user
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfTheUser(@PathVariable  String userId){
        List<OrderDto> orderOfUser = orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orderOfUser,HttpStatus.OK);
    }


    // get all orders
    @GetMapping
    public ResponseEntity <PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "billingName",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity(orders,HttpStatus.OK);
    }

    // update the orders
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable  String orderId,@RequestBody OrderDto orderDto){
        OrderDto orderDto1 = orderService.updateOrder(orderId, orderDto);
        return new ResponseEntity<>(orderDto1,HttpStatus.OK);
    }
}
