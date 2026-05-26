package com.splithome.backend.auth.service;

import com.splithome.backend.auth.dto.request.LoginRequest;
import com.splithome.backend.auth.dto.response.MeResponse;
import com.splithome.backend.exception.customs.InvalidCredentialsException;
import com.splithome.backend.property.repository.PropertyRepository;
import com.splithome.backend.tenancy.entity.TenancyMember;
import com.splithome.backend.tenancy.repository.TenancyMemberRepository;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final PropertyRepository propertyRepository;

    private final TenancyMemberRepository tenancyMemberRepository;


    // LOGIN NA PLATAFORMA
    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        Optional<TenancyMember> membership = tenancyMemberRepository.findByUser(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("isOwner", propertyRepository.existsByOwner(user));

        if (membership.isPresent()) {
            TenancyMember member = membership.get();
            claims.put("isMember", true);
            claims.put("tenancyId", member.getTenancy().getId().toString());
        } else {
            claims.put("isMember", false);
            claims.put("tenancyId", null);
        }

        return jwtService.generateToken(user, claims);
    }

    // DADOS DO USUÁRIO AUTENTICADO
    public MeResponse getMe(User user) {
        boolean isOwner = propertyRepository.existsByOwner(user);
        Optional<TenancyMember> membership = tenancyMemberRepository.findByUser(user);

        boolean isMember = membership.isPresent();
        boolean isHead = membership.map(TenancyMember::isHead).orElse(false);

        return new MeResponse(user.getId(), user.getName(), user.getEmail(), isOwner, isMember, isHead);
    }

}
