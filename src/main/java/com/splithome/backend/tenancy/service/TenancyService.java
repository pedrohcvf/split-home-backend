package com.splithome.backend.tenancy.service;

import com.splithome.backend.auth.service.JwtService;
import com.splithome.backend.exception.customs.*;
import com.splithome.backend.property.entity.Property;
import com.splithome.backend.property.repository.PropertyRepository;
import com.splithome.backend.tenancy.dto.request.TenancyRequest;
import com.splithome.backend.tenancy.dto.response.TenancyResponse;
import com.splithome.backend.tenancy.entity.Tenancy;
import com.splithome.backend.tenancy.entity.TenancyMember;
import com.splithome.backend.tenancy.repository.TenancyMemberRepository;
import com.splithome.backend.tenancy.repository.TenancyRepository;
import com.splithome.backend.user.entity.User;
import com.splithome.backend.util.InviteCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenancyService {

    private final TenancyRepository tenancyRepository;

    private final TenancyMemberRepository tenancyMemberRepository;

    private final JwtService jwtService;

    private final PropertyRepository propertyRepository;

    // CRIAR TENANCY
    public TenancyResponse createTenancy(TenancyRequest request){
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Property property = propertyRepository.findById(request.propertyId())
                .orElseThrow(()-> new PropertyNotFoundException(request.propertyId()));

        if (!property.getOwner().getId().equals(owner.getId())){
            throw new AccessDeniedException("Você não tem permissão nesse imóvel");
        }

        if (tenancyRepository.existsByPropertyAndActive(property, true)){
            throw new TenancyAlreadyExistsException(property.getAddress());
        }

        if (!property.isAvailable()){
            throw new PropertyUnavailableException(property.getAddress());
        }

        String inviteCode = InviteCodeGenerator.generateInviteCode();

        Tenancy tenancy = Tenancy.builder().property(property).name(request.name()).inviteCode(inviteCode).build();

        Tenancy savedTenancy = tenancyRepository.save(tenancy);

        property.setAvailable(false);

        propertyRepository.save(property);

        String token = jwtService.generateToken(owner);

        return new TenancyResponse(savedTenancy.getId(),
                savedTenancy.getInviteCode(),
                savedTenancy.getProperty().getAddress(),
                savedTenancy.isActive(),
                token);
    }

    // ENTRAR PELO INVITE CODE
    public TenancyResponse joinByInviteCode(String inviteCode){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Tenancy tenancy = tenancyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new TenancyNotFoundException());

        if (!tenancy.isActive()){
            throw new TenancyInactiveException(inviteCode);
        }

        if (tenancyMemberRepository.existsByTenancyAndUser(tenancy, user)){
            throw new UserAlreadyMemberException(user.getEmail());
        }

        boolean head = !tenancyMemberRepository.existsByTenancy(tenancy);

        TenancyMember member = TenancyMember.builder().tenancy(tenancy).user(user).head(head).build();

        tenancyMemberRepository.save(member);

        String token = jwtService.generateToken(user);

        return new TenancyResponse(tenancy.getId(),
                tenancy.getInviteCode(),
                tenancy.getProperty().getAddress(),
                tenancy.isActive(),
                token);
    }

    // RESGATAR INVITE CODE
    public TenancyResponse rescueInviteCode(UUID id){
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Tenancy tenancy = tenancyRepository.findById(id)
                .orElseThrow(() -> new TenancyNotFoundException());

        if (!tenancy.getProperty().getOwner().getId().equals(owner.getId()) &&
                !tenancyMemberRepository.existsByTenancyAndUserAndHead(tenancy, owner, true)){
            throw new AccessDeniedException("Você não tem permissão para resgatar o invite code do imóvel");
        }

        return new TenancyResponse(tenancy.getId(),
                tenancy.getInviteCode(),
                tenancy.getProperty().getAddress(),
                tenancy.isActive(),
                null);
    }
}
