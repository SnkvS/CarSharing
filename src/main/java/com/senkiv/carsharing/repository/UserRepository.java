package com.senkiv.carsharing.repository;

import com.senkiv.carsharing.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph("user-with-roles")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
