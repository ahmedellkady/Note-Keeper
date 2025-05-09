package com.example.note_keeper.domain.repository;

import com.example.note_keeper.domain.model.NoteVersion;
import java.util.List;
import java.util.Optional;

public interface NoteVersionRepository {
    NoteVersion save(NoteVersion version);

    List<NoteVersion> findByNoteId(Long noteId);
    
    Optional<NoteVersion> findById(Long id);
}
