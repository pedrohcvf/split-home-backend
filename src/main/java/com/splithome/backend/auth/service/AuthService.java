package com.splithome.backend.auth.service;

import com.splithome.backend.auth.dto.request.LoginRequest;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    // LOGIN NA PLATAFORMA
    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new RuntimeException("Senha incorreta");
        }

        return jwtService.generateToken(user);
    }

}
