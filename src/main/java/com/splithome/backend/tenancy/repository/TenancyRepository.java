package com.splithome.backend.tenancy.repository;

import com.splithome.backend.property.entity.Property;
import com.splithome.backend.tenancy.entity.Tenancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenancyRepository extends JpaRepository<Tenancy, UUID> {

    Optional<Tenancy> findByInviteCode(String inviteCode);

    boolean existsByPropertyAndActive(Property property,boolean active);

}
