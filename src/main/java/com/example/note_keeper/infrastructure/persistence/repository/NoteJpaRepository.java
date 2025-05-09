package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.domain.model.Note;
import com.example.note_keeper.domain.repository.NoteRepository;
import com.example.note_keeper.infrastructure.persistence.entity.NoteEntity;
import org.springframework.stereotype.Repository;

@Repository
public class NoteJpaRepository implements NoteRepository {
    private final NoteJpaInterface jpa;

    public NoteJpaRepository(NoteJpaInterface jpa) {
        this.jpa = jpa;
    }

    @Override
    public Note save(Note note) {
        NoteEntity entity = new NoteEntity();
        entity.setTitle(note.getTitle());
        entity.setContent(note.getContent());
        entity.setUserId(note.getUserId());
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
        result.setUserId(saved.getUserId());
        result.setTag(saved.getTag());

        return result;
    }
}
