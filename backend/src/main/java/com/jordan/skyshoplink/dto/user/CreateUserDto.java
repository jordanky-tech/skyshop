package com.jordan.skyshoplink.dto.user;

public record CreateUserDto(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
