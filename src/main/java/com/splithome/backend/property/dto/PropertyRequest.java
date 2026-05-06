package com.splithome.backend.property.dto;

import jakarta.validation.constraints.NotBlank;

public record PropertyRequest (@NotBlank String address,
                               String description){
}
