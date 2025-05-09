package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.infrastructure.persistence.entity.NoteEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteJpaInterface extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findByUserId(Long userId);
}
