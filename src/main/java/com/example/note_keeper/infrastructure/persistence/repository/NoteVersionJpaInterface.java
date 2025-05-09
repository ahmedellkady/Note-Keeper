package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.infrastructure.persistence.entity.NoteVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteVersionJpaInterface extends JpaRepository<NoteVersionEntity, Long> {
    List<NoteVersionEntity> findByNoteId(Long noteId);
}
