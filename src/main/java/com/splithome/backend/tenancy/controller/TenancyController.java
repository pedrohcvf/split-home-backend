package com.splithome.backend.tenancy.controller;

import com.splithome.backend.tenancy.dto.request.TenancyRequest;
import com.splithome.backend.tenancy.dto.response.TenancyResponse;
import com.splithome.backend.tenancy.service.TenancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenancy")
@RequiredArgsConstructor
public class TenancyController {

    private final TenancyService tenancyService;

    // CRIAR TENANCY
    @PostMapping
    public ResponseEntity<TenancyResponse> createTenancy(@Valid @RequestBody TenancyRequest request){
        TenancyResponse tenancyResponse = tenancyService.createTenancy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenancyResponse);
    }

    // ENTRAR POR INVITE CODE
    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<TenancyResponse> joinByInviteCode(@PathVariable String inviteCode){
        TenancyResponse tenancyResponse = tenancyService.joinByInviteCode(inviteCode);
        return ResponseEntity.status(HttpStatus.OK).body(tenancyResponse);
    }
}
