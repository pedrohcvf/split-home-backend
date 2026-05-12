package com.splithome.backend.tenancy.mapper;

import com.splithome.backend.tenancy.dto.response.TenancyResponse;
import com.splithome.backend.tenancy.entity.Tenancy;
import org.springframework.stereotype.Component;

@Component
public class TenancyMapper {

    public TenancyResponse toResponse(Tenancy tenancy, String token){
        return new TenancyResponse(tenancy.getId(),
                tenancy.getInviteCode(),
                tenancy.getProperty().getAddress(),
                tenancy.isActive(),
                token);
    }

}
