package com.senkiv.carsharing.repository;

import com.senkiv.carsharing.model.Role;
import com.senkiv.carsharing.model.Role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
