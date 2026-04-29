package com.splithome.backend.user.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id,
                           String name,
                           String email,
                           LocalDateTime createdAt) {
}
