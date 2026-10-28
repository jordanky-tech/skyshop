package com.jordan.skyshoplink.dto.cartItem;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartItemResponse {
    private UUID cartId;
    private UUID cartItemId;
    private UUID productId;
    private Integer quantity;
}
