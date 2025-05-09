package com.example.note_keeper.domain.repository;

import com.example.note_keeper.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
