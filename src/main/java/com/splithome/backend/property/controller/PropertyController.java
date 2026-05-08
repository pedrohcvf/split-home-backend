package com.splithome.backend.property.controller;

import com.splithome.backend.property.dto.PropertyAvailabilityRequest;
import com.splithome.backend.property.dto.PropertyRequest;
import com.splithome.backend.property.dto.PropertyCreateResponse;
import com.splithome.backend.property.dto.PropertyResponse;
import com.splithome.backend.property.entity.Property;
import com.splithome.backend.property.service.PropertyService;
import com.splithome.backend.user.dto.response.UserResponse;
import com.splithome.backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;

    // CRIAR IMÓVEL
    @PostMapping
    public ResponseEntity<PropertyCreateResponse> createProperty(@Valid @RequestBody PropertyRequest request){
        PropertyCreateResponse property = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }

    // LISTAR IMÓVEIS POR PROPRIETÁRIO
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> listAllProperties(){
        List<PropertyResponse> listProperties = propertyService.listAllProperties();
        return ResponseEntity.status(HttpStatus.OK).body(listProperties);
    }

    // DELETAR IMÓVEL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id){
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    // ATUALIZAR DISPONIBILIDADE
    @PatchMapping("/{id}/availability")
    public ResponseEntity<PropertyResponse> changeAvailability(@PathVariable UUID id, @RequestBody PropertyAvailabilityRequest request){
        PropertyResponse property = propertyService.changeAvailability(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(property);
    }



}
