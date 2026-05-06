package com.splithome.backend.property.dto;

import java.util.UUID;

public record PropertyResponse(UUID id,
                               String address,
                               String description,
                               boolean available,
                               String ownerName,
                               String ownerEmail) {
}
