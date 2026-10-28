package com.jordan.skyshoplink.service;


import com.jordan.skyshoplink.dto.cart.CartResponseDto;
import com.jordan.skyshoplink.entity.Cart;
import com.jordan.skyshoplink.entity.CartItem;
import com.jordan.skyshoplink.entity.Product;
import com.jordan.skyshoplink.repository.CartItemRepository;
import com.jordan.skyshoplink.repository.CartRepository;
import com.jordan.skyshoplink.repository.ProductRepository;
import com.jordan.skyshoplink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public CartResponseDto addToCart(UUID userId, AddRequestDto addRequestDto){
        Cart cart = this.cartRepository.findByUserId(userId)
                .orElseGet(()->this.createCartForUser(userId));
        Product product = this.productRepository.getReferenceById(addRequestDto.productId());

        if (product.getStockQuantity() < addRequestDto.quantity()){
            throw new RuntimeException("votre demande est supperieur au stock !");
        }

        CartItem cartItem = this.cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(()-> CartItem.builder()
                        .cart(this.cartRepository.getReferenceById(cart.getId()))
                        .product(this.productRepository.getReferenceById(product.getId()))
                        .quantity(0)
                        .build());
        cartItem.setQuantity(addRequestDto.quantity);
        this.cartItemRepository.save(cartItem);
    }

    public CartResponseDto getCart(UUID userId) {
        Cart cart = this.cartRepository.findByUserId(userId).orElseGet(
                () -> {
                    return this.createCartForUser(userId);
                }
        );
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .build();
    }

    public Cart createCartForUser(UUID userId) {
        Cart cart = this.cartRepository.findByUserId(userId).orElseGet(
                () -> {
                    return Cart.builder()
                            .user(this.userRepository.getReferenceById(userId))
                            .build();
                }
        );
        return this.cartRepository.save(cart);
    }
}
