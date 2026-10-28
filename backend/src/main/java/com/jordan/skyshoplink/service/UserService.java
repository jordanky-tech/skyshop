package com.jordan.skyshoplink.service;

import com.jordan.skyshoplink.dto.user.CreateUserDto;
import com.jordan.skyshoplink.dto.user.UserResponseDto;
import com.jordan.skyshoplink.ennum.Role;
import com.jordan.skyshoplink.entity.User;
import com.jordan.skyshoplink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto signin(CreateUserDto data){
        if (this.userRepository.existsByEmail(data.email())){
            throw new RuntimeException("user alrady exist");
        }
        User user = User.builder().lastname(data.lastname())
                .firstname(data.firstname())
                .email(data.email())
                .password(passwordEncoder.encode(data.password()))
                .role(Role.ROLE_CLIENT)
                .build();
        User saved = this.userRepository.save(user);

        return UserResponseDto.builder().email(saved.getEmail()).build();


    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }
}
