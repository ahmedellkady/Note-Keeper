package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.domain.model.NoteVersion;
import com.example.note_keeper.domain.repository.NoteVersionRepository;
import com.example.note_keeper.infrastructure.persistence.entity.NoteEntity;
import com.example.note_keeper.infrastructure.persistence.entity.NoteVersionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NoteVersionJpaRepository implements NoteVersionRepository {

    private final NoteVersionJpaInterface jpa;

    public NoteVersionJpaRepository(NoteVersionJpaInterface jpa) {
        this.jpa = jpa;
    }

    @Override
    public NoteVersion save(NoteVersion version) {
        NoteVersionEntity entity = new NoteVersionEntity();
        NoteEntity note = new NoteEntity();
        note.setId(version.getNoteId());

        entity.setNote(note);
        entity.setTitle(version.getTitle());
        entity.setContent(version.getContent());
        entity.setCreatedAt(version.getCreatedAt());

        NoteVersionEntity saved = jpa.save(entity);
        version.setId(saved.getId());
        return version;
    }

    @Override
    public List<NoteVersion> findByNoteId(Long noteId) {
        return jpa.findByNoteId(noteId).stream().map((NoteVersionEntity e) -> {
            NoteVersion version = new NoteVersion();
            version.setId(e.getId());
            version.setNoteId(e.getNote().getId());
            version.setTitle(e.getTitle());
            version.setContent(e.getContent());
            version.setCreatedAt(e.getCreatedAt());
            return version;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<NoteVersion> findById(Long versionId) {
        return jpa.findById(versionId).map(e -> {
            NoteVersion v = new NoteVersion();
            v.setId(e.getId());
            v.setNoteId(e.getNote().getId());
            v.setTitle(e.getTitle());
            v.setContent(e.getContent());
            v.setCreatedAt(e.getCreatedAt());
            return v;
        });
    }

}
