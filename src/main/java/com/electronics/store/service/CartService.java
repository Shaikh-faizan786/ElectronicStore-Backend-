package com.electronics.store.service;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.CartDto;

public interface  CartService {

    // hum do tarikse se cart me item ko daal sakte hai
    // 1: user ka cart available nhi hoga to hum pehle cart banayenge then usme item ko add karenge
    // 2: pehle se hi cart bana hoga bas usme  item add kara hai

    // item ko cart me add kar rahe hai
    CartDto addItemCart(String userId, AddItemToCartRequest request );

    // item ko cart se nikal rahe hai
    void removeItemFromCart(String userId,int cartItem);

    // remove all item from card
    void clearCart(String userId);

    // get user cart onuy
    CartDto getCartByUser(String userId);

}
