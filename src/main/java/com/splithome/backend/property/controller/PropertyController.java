package com.splithome.backend.property.controller;

import com.splithome.backend.property.dto.PropertyRequest;
import com.splithome.backend.property.dto.PropertyResponse;
import com.splithome.backend.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;

    // CRIAR IMÓVEL
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest request){
        PropertyResponse property = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }



}
