package com.splithome.backend.tenancy.repository;

import com.splithome.backend.property.entity.Property;
import com.splithome.backend.tenancy.entity.Tenancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface TenancyRepository extends JpaRepository<Tenancy, UUID> {

    Optional<Tenancy> findByInviteCode(String inviteCode);

    boolean existsByPropertyAndActive(Property property,boolean active);

    @Query("SELECT t FROM Tenancy t JOIN FETCH t.property WHERE t.id= :id")
    Optional<Tenancy> findByIdWithProperty(@Param("id") UUID id);

    @Query("SELECT t FROM Tenancy t JOIN FETCH t.property WHERE t.inviteCode = :inviteCode")
    Optional<Tenancy> findByInviteCodeWithProperty(@Param("inviteCode") String inviteCode);

}
