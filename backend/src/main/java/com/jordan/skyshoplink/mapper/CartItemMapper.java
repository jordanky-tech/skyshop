package com.jordan.skyshoplink.mapper;

import com.jordan.skyshoplink.dto.cartItem.CartItemResponse;
import com.jordan.skyshoplink.dto.cartItem.CreateCartItemDto;
import com.jordan.skyshoplink.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    public CartItemResponse toResponse(CartItem data){
        return CartItemResponse.builder()
                .cartId(data.getCart().getId())
                .cartItemId(data.getId())
                .productId(data.getProduct().getId())
                .quantity(data.getQuantity()).build();
    }

}
