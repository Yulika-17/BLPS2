package com.nullnumber1.lab1.repository;

import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> getUsersByRolesContaining(RoleEnum role);
}