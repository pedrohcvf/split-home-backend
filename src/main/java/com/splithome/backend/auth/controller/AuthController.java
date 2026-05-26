package com.splithome.backend.auth.controller;

import com.splithome.backend.auth.dto.request.LoginRequest;
import com.splithome.backend.auth.dto.request.RegisterRequest;
import com.splithome.backend.auth.dto.response.MeResponse;
import com.splithome.backend.auth.service.AuthService;
import com.splithome.backend.user.dto.response.UserResponse;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.user.mapper.UserMapper;
import com.splithome.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final AuthService authService;

    // REGISTRAR USUÁRIO
    @PostMapping("/register")
    public ResponseEntity<UserResponse> userRegister(@Valid @RequestBody RegisterRequest request){
        User user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request){
        String token = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    // DADOS DO USUÁRIO AUTENTICADO
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(authService.getMe(user));
    }

}
