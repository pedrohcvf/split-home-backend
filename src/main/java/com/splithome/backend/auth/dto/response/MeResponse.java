package com.splithome.backend.auth.dto.response;

import java.util.UUID;

public record MeResponse(UUID id,
                         String name,
                         String email,
                         boolean isOwner,
                         boolean isMember,
                         boolean isHead) {
}
