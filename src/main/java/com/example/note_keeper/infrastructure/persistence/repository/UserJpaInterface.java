package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserJpaInterface extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
