package com.educative.app.hlv.repo;

import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends AppRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.career WHERE u.role = :role AND u.active = true")
    List<User> findByRoleWithCareer(@Param("role") Role role);

    List<User> findByRoleAndActiveTrue(Role role);

    boolean existsByRegistrationNumber(String registrationNumber);
}
