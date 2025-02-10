package com.electronics.store.controllers;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.ApiResponseMessage;
import com.electronics.store.dtos.CartDto;
import com.electronics.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
   private  CartService cartService;

    // Add Item from the cart
    @PostMapping("{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest addItemToCartRequest){
        CartDto cartDto = cartService.addItemCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    // remove particular Item from the cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemfromCart(@PathVariable String userId, @PathVariable  int itemId){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is Removed")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // remove whole Item from the cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Cart is Empty")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get user cart only
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId){
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser, HttpStatus.OK);
    }

}
