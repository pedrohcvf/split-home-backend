package com.splithome.backend.property.repository;

import com.splithome.backend.property.entity.Property;
import com.splithome.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    List<Property> findByOwner(User owner);

    boolean existsByOwner(User owner);

    boolean existsByOwnerAndAddress(User owner, String address);

}
