package com.splithome.backend.tenancy.repository;

import com.splithome.backend.tenancy.entity.Tenancy;
import com.splithome.backend.tenancy.entity.TenancyMember;
import com.splithome.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenancyMemberRepository extends JpaRepository<TenancyMember, UUID> {

    boolean existsByTenancyAndUser(Tenancy tenancy, User user);

    List<TenancyMember> findByTenancy(Tenancy tenancy);

    Optional<TenancyMember> findFirstByTenancyAndHeadFalseOrderByJoinedAtAsc(Tenancy tenancy);

    boolean existsByUser(User user);

    boolean existsByTenancy(Tenancy tenancy);

    boolean existsByTenancyAndUserAndHead(Tenancy tenancy, User user, boolean head);

    List<TenancyMember> findAllByTenancy(Tenancy tenancy);

    Optional<TenancyMember> findByUser(User user);

}
