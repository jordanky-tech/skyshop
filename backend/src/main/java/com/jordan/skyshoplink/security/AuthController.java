package com.jordan.skyshoplink.security;

import com.jordan.skyshoplink.common.response.ApiResponse;
import com.jordan.skyshoplink.common.response.ResponseBuilder;
import com.jordan.skyshoplink.dto.user.AuthDto;
import com.jordan.skyshoplink.dto.user.CreateUserDto;
import com.jordan.skyshoplink.dto.user.UserResponseDto;
import com.jordan.skyshoplink.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "authentification et connexion", description = "authentification et connexion")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(
            summary = "s'inscre ou encore créer un utilisateur",
            description = "s'inscre ou encore créer un utilisateur")
    public ResponseEntity<ApiResponse<UserResponseDto>> signin(@RequestBody CreateUserDto data){
        return ResponseBuilder.created(this.userService.signin(data), "utilisateur inscrit avec success !");
    }

    @PostMapping("/login")
    @Operation(
            summary = "se connecter ",
            description = "se connecter ")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody AuthDto data){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.email(), data.password())
        );

        if (authentication.isAuthenticated()){
            Map<String, String> token = this.jwtService.generate(data.email());
            return ResponseBuilder.success(token, "utilisateur connecté avec success !");
        }
        return null;

    }

}
