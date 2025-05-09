package com.example.note_keeper.domain.repository;

import com.example.note_keeper.domain.model.Note;

public interface NoteRepository {
    Note save(Note note);
}
