package com.jordan.skyshoplink.service;


import com.jordan.skyshoplink.dto.cartItem.CartItemResponse;
import com.jordan.skyshoplink.dto.cartItem.CreateCartItemDto;
import com.jordan.skyshoplink.entity.CartItem;
import com.jordan.skyshoplink.mapper.CartItemMapper;
import com.jordan.skyshoplink.repository.CartItemRepository;
import com.jordan.skyshoplink.repository.CartRepository;
import com.jordan.skyshoplink.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;

    public List<CartItemResponse> getAll(){
        List<CartItem> response = this.cartItemRepository.findAll();
        return response.stream().map(cartItem ->{
            return CartItemResponse.builder()
                    .cartId(cartItem.getCart().getId())
                    .cartItemId(cartItem.getId())
                    .productId(cartItem.getProduct().getId())
                    .quantity(cartItem.getQuantity()).build();
        }).toList();
    }

    public CartItemResponse getById(UUID id){
        CartItem response = this.cartItemRepository.getReferenceById(id);
        return CartItemResponse.builder()
                .cartId(response.getCart().getId())
                .cartItemId(response.getId())
                .productId(response.getProduct().getId())
                .quantity(response.getQuantity()).build();
    }

    public CartItemResponse create(CreateCartItemDto data){
        CartItem request = CartItem.builder()
                .cart(this.cartRepository.getReferenceById(data.cartId()))
                .product(this.productRepository.getReferenceById(data.productId()))
                .quantity(data.quantity())
                .build();
        return this.cartItemMapper.toResponse(this.cartItemRepository.save(request));
    }

}
