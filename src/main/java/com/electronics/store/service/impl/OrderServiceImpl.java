package com.electronics.store.service.impl;
import com.electronics.store.dtos.CreateOrderRequest;
import com.electronics.store.dtos.OrderDto;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.entities.*;
import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.helper.Helper;
import com.electronics.store.repositories.CartRepository;
import com.electronics.store.repositories.OrderRepository;
import com.electronics.store.repositories.UserReposiory;
import com.electronics.store.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserReposiory userReposiory;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;


    // create order
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        // user ka id lenge
        String userId = orderDto.getUserId();
        // cart ka id lenge
        String cartId = orderDto.getCartId();

        // 1 fetch the user
        User user = userReposiory.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found With The Given Id"));

        // 2 feth the cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourcseNotFoundException("cart Id is Not Found With The Given Id"));


        // 3  cart ke sare items ke list ajayenge
        List<CartItem> cartItems = cart.getItems();

        // 4 agar cart me item zero hoga to ye exception throw karenge
        if(cartItems.size()<=0){
            throw new BadApiRequestException("Cart is empty");
        }

        // 5 agar cart me items hoga to order create karenge
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        // 12 amoutn ko set karne ke liye ye variable banaya gaya hai
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

        // 6 ab cartItem ki madad se orderItem set karenge
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                    .orders(order)
                    .build();
            // 13 amount ko set karne ke liye upar ek variable declare kiye hai jsime amount ka value zero hai
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return  orderItem;
        }).collect(Collectors.toList());

        // 7 jab orderitem miljayega to order ko set karenge
        order.setOrderItem(orderItems);
        order.setOrdeAmount(orderAmount.get());

        // 8 ab cart ko empty kardenge
        cart.getItems().clear();

        // 9 cart ko save karenge
        cartRepository.save(cart);

        // 10 order ko save karenge
        Order saveOrder = orderRepository.save(order);

        // 11 orderDto me convert kar denge
        return mapper.map(saveOrder,OrderDto.class);
    }


    // remove order
    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourcseNotFoundException("Order Id is not found"));
        orderRepository.delete(order);
    }

    @Override
    public List <OrderDto> getOrderOfUser(String userId) {
        User user = userReposiory.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User id is not found with the given id "));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    // get all orders
    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(orders,OrderDto.class);

    }

    @Override
    public OrderDto updateOrder( String orderId,OrderDto orderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourcseNotFoundException("order is not found with the given id"));
//        AtomicReference<Integer> updatedAmount= new AtomicReference<>(0);
       order.setOrderStatus(orderDto.getOrderStatus());
       order.setPaymentStatus(orderDto.getPaymentStatus());
       order.setBillingAddress(orderDto.getBillingAddress());
       order.setBillingName(orderDto.getBillingName());
       order.setBillingPhone(orderDto.getBillingPhone());
        Order updatedOrders = orderRepository.save(order);
        return mapper.map(updatedOrders, OrderDto.class);
    }
}
