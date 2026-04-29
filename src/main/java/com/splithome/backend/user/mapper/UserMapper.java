package com.splithome.backend.user.mapper;

import com.splithome.backend.auth.dto.request.RegisterRequest;
import com.splithome.backend.user.dto.response.UserResponse;
import com.splithome.backend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    // CONVERSÃO PARA DTO
    public UserResponse toDto(User user){
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    // CONVERSÃO PARA ENTIDADE
    public User toEntity(RegisterRequest request, String hashedPassword){
        return User.builder().name(request.name())
                .email(request.email())
                .passwordHash(hashedPassword)
                .build();
    }


}
