package com.splithome.backend.user.service;

import com.splithome.backend.auth.dto.request.RegisterRequest;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.user.mapper.UserMapper;
import com.splithome.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;


    // REGISTRAR USUARIO
    public User register(RegisterRequest request){

        if (userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email já cadastrado.");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        return userRepository.save(userMapper.toEntity(request,hashedPassword));
    }



}
