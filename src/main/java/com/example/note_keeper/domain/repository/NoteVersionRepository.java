package com.example.note_keeper.domain.repository;

import com.example.note_keeper.domain.model.NoteVersion;
import java.util.List;

public interface NoteVersionRepository {
    NoteVersion save(NoteVersion version);
    List<NoteVersion> findByNoteId(Long noteId);
}
