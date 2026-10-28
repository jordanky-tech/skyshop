package com.jordan.skyshoplink.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartResponseDto {
    private UUID cartId;
    private UUID userId;
}
