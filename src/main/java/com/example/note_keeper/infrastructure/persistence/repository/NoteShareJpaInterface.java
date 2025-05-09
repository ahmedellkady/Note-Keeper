package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.infrastructure.persistence.entity.NoteShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteShareJpaInterface extends JpaRepository<NoteShareEntity, Long> {
    List<NoteShareEntity> findBySharedWithUser_Id(Long userId);
}
