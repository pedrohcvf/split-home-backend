package com.splithome.backend.tenancy.dto.response;
import java.util.UUID;

public record TenancyResponse(UUID tenancyId,
                              String inviteCode,
                              String propertyAddress,
                              boolean active,
                              String token) {
}
