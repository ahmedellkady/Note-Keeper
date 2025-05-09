package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.infrastructure.persistence.entity.NoteShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteShareJpaInterface extends JpaRepository<NoteShareEntity, Long> {
    List<NoteShareEntity> findBySharedWithUser_Id(Long userId);
    Optional<NoteShareEntity> findByNote_IdAndSharedWithUser_Email(Long noteId, String email);
}
