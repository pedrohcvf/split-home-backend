package com.splithome.backend.property.service;

import com.splithome.backend.auth.service.JwtService;
import com.splithome.backend.exception.customs.PropertyAlreadyExistsException;
import com.splithome.backend.exception.customs.PropertyNotFoundException;
import com.splithome.backend.property.dto.PropertyAvailabilityRequest;
import com.splithome.backend.property.dto.PropertyRequest;
import com.splithome.backend.property.dto.PropertyCreateResponse;
import com.splithome.backend.property.dto.PropertyResponse;
import com.splithome.backend.property.entity.Property;
import com.splithome.backend.property.repository.PropertyRepository;
import com.splithome.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    private final JwtService jwtService;

    // CRIAR IMÓVEL
    public PropertyCreateResponse createProperty(PropertyRequest request){
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(propertyRepository.existsByOwnerAndAddress(owner,request.address())){
            throw new PropertyAlreadyExistsException(request.address());
        }

        Property property = Property.builder()
                            .owner(owner)
                            .address(request.address())
                            .description(request.description())
                            .build();

        Property savedProperty = propertyRepository.save(property);

        Map<String, Object> claims = new HashMap<>();
        claims.put("isOwner", true);
        claims.put("isMember", false);
        claims.put("email", owner.getEmail());

        String token = jwtService.generateToken(owner, claims);

        return new PropertyCreateResponse(savedProperty.getId(),
                                    savedProperty.getAddress(),
                                    savedProperty.getDescription(),
                                    savedProperty.isAvailable(),
                                    savedProperty.getOwner().getName(),
                                    savedProperty.getOwner().getEmail(),
                                    token);
    }

    // LISTAR TODOS OS IMÓVEIS POR PROPRIETÁRIO
    public List<PropertyResponse> listAllProperties(){
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Property> properties = propertyRepository.findByOwner(owner);
        return properties.stream()
                .map(p -> new PropertyResponse(
                        p.getId(),
                        p.getAddress(),
                        p.getDescription(),
                        p.isAvailable(),
                        owner.getName(),
                        owner.getEmail()
                ))
                .toList();
    }

    // DELETAR IMÓVEL
    public void deleteProperty(UUID id){
        Property property = propertyRepository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!property.getOwner().getId().equals(owner.getId())){
            throw new AccessDeniedException("Você não tem permissão para deletar esse imóvel");
        }

        propertyRepository.deleteById(id);
    }

    // ALTERAR DISPONIBILIDADE
    public PropertyResponse changeAvailability(UUID id, PropertyAvailabilityRequest request){
        Property property = propertyRepository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!property.getOwner().getId().equals(owner.getId())){
            throw  new AccessDeniedException("Voce não tem permissão para editar esse imóvel");
        }

        property.setAvailable(request.available());

        Property savedProperty = propertyRepository.save(property);

        return new PropertyResponse(
                savedProperty.getId(),
                savedProperty.getAddress(),
                savedProperty.getDescription(),
                savedProperty.isAvailable(),
                owner.getName(),
                owner.getEmail());
    }

}
