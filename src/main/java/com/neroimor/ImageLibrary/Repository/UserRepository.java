package com.neroimor.ImageLibrary.Repository;

import com.neroimor.ImageLibrary.Models.UsersModels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
