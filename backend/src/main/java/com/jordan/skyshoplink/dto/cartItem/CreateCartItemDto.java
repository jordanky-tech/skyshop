package com.jordan.skyshoplink.dto.cartItem;

import java.util.UUID;

public record CreateCartItemDto (
        UUID cartId,
        UUID productId,
        Integer quantity
){

}
