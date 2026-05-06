package com.splithome.backend.auth.service;

import com.splithome.backend.auth.dto.request.LoginRequest;
import com.splithome.backend.exception.InvalidCredentialsException;
import com.splithome.backend.property.repository.PropertyRepository;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final PropertyRepository propertyRepository;


    // LOGIN NA PLATAFORMA
    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("isOwner", propertyRepository.existsByOwner(user));
        claims.put("isMember", false);

        return jwtService.generateToken(user, claims);
    }

}
