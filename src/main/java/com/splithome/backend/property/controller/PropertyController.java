package com.splithome.backend.property.controller;

import com.splithome.backend.property.dto.PropertyRequest;
import com.splithome.backend.property.dto.PropertyCreateResponse;
import com.splithome.backend.property.dto.PropertyResponse;
import com.splithome.backend.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



}
