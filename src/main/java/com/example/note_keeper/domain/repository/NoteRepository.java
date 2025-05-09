package com.example.note_keeper.domain.repository;

import java.util.Optional;

import com.example.note_keeper.domain.model.Note;

public interface NoteRepository {
    Note save(Note note);
    Optional<Note> findById(Long id);
}
