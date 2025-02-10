package com.electronics.store.service.impl;
import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.CartDto;
import com.electronics.store.entities.Cart;
import com.electronics.store.entities.CartItem;
import com.electronics.store.entities.Product;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.repositories.CartItemRepository;
import com.electronics.store.repositories.CartRepository;
import com.electronics.store.repositories.ProductRepository;
import com.electronics.store.repositories.UserReposiory;
import com.electronics.store.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserReposiory userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public CartDto addItemCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        // 1 pehle product ki id nikala
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcseNotFoundException("Product id is not found in the database"));

        // 2 user ka id nikala
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("user is not found "));

        // 3 fir ys user ka cart bana hai ya nhi chcek karenge agar cart bana hoga to theek warna new cart bana denge
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        // 10 ye variable iske liye banaya hai ki cart me wo item already hai ya nhi check karne ke liye
        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        // 4 agar cart bana hoga to uske cart items ko dikha denge
        List<CartItem> items = cart.getItems();

        // 9 agar cart me already itesm rahega jo item hum set kar rahe hai to waise me sirf product or quamtity ko badha denge
        items = items.stream().map(item -> {
            if (product.getProductId().equals(productId)) {
                // item already cart me hai
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
//        cart.setItems(updatedItems);

        //  5 or agar cart me item nhi hoga to item set kardenge cart me
        if (!updated.get()) {
            CartItem cartItems = CartItem.builder()
                    .product(product)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItems);
        }

        // 6 cart ko set kar denge user ke isme
        cart.setUser(user);
        // 7 or cart ko save kardenge
        Cart updatedCart = cartRepository.save(cart);
        // 8 or ye hum ko cart return karega to hum isko car Dto me convert kardenge
        return mapper.map(updatedCart, CartDto.class);
    }


    // remove item from cart
    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourcseNotFoundException("CarT Item is not Found"));
        cartItemRepository.delete(cartItem1);
    }

    // clear cart
    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found In Database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourcseNotFoundException("Cart Of Gven user Not Found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found In Database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourcseNotFoundException("Cart Of Gven user Not Found"));
        return mapper.map(cart,CartDto.class);
    }
}

