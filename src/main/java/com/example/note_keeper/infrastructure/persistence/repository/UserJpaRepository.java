package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.domain.model.User;
import com.example.note_keeper.domain.repository.UserRepository;
import com.example.note_keeper.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJpaRepository implements UserRepository {
    private final UserJpaInterface jpa;

    public UserJpaRepository(UserJpaInterface jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email)
                  .map(e -> new User(e.getId(), e.getName(), e.getEmail(), e.getPassword()));
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());

        UserEntity saved = jpa.save(entity);
        return new User(saved.getId(), saved.getName(), saved.getEmail(), saved.getPassword());
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id)
                  .map(e -> new User(e.getId(), e.getName(), e.getEmail(), e.getPassword()));
    }
}
