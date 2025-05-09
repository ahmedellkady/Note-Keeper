package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.domain.model.Note;
import com.example.note_keeper.domain.repository.NoteRepository;
import com.example.note_keeper.infrastructure.persistence.entity.NoteEntity;
import com.example.note_keeper.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class NoteJpaRepository implements NoteRepository {
    private final NoteJpaInterface jpa;
    private final UserJpaInterface userJpaInterface;

    public NoteJpaRepository(NoteJpaInterface jpa, UserJpaInterface userJpaInterface) {
        this.jpa = jpa;
        this.userJpaInterface = userJpaInterface;
    }

    @Override
    public Note save(Note note) {
        NoteEntity entity;

        if (note.getId() == null) {
            entity = new NoteEntity();
        } else {
            entity = jpa.findById(note.getId()).orElse(new NoteEntity());
            entity.setId(note.getId());
        }

        UserEntity user = userJpaInterface.findById(note.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + note.getUserId()));

        entity.setTitle(note.getTitle());
        entity.setContent(note.getContent());
        entity.setUser(user);
        entity.setCreatedAt(note.getCreatedAt());
        entity.setUpdatedAt(note.getUpdatedAt());
        entity.setTag(note.getTag());

        NoteEntity saved = jpa.save(entity);

        Note result = new Note();
        result.setId(saved.getId());
        result.setTitle(saved.getTitle());
        result.setContent(saved.getContent());
        result.setCreatedAt(saved.getCreatedAt());
        result.setUpdatedAt(saved.getUpdatedAt());
        result.setUserId(saved.getUser().getId());
        result.setTag(saved.getTag());

        return result;
    }

    @Override
    public Optional<Note> findById(Long id) {
        return jpa.findById(id).map(entity -> {
            Note note = new Note();
            note.setId(entity.getId());
            note.setTitle(entity.getTitle());
            note.setContent(entity.getContent());
            note.setCreatedAt(entity.getCreatedAt());
            note.setUpdatedAt(entity.getUpdatedAt());
            note.setUserId(entity.getUser().getId());
            note.setTag(entity.getTag());
            return note;
        });
    }

}
