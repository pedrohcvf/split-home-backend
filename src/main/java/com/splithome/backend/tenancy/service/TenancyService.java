package com.splithome.backend.tenancy.service;

import com.splithome.backend.auth.service.JwtService;
import com.splithome.backend.property.repository.PropertyRepository;
import com.splithome.backend.property.service.PropertyService;
import com.splithome.backend.tenancy.repository.TenancyMemberRepository;
import com.splithome.backend.tenancy.repository.TenancyRepository;
import com.splithome.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenancyService {

    private final TenancyRepository tenancyRepository;

    private final TenancyMemberRepository tenancyMemberRepository;

    private final JwtService jwtService;

    private final PropertyRepository propertyRepository;

    private final UserRepository userRepository;




}
