package com.example.note_keeper.domain.repository;

import com.example.note_keeper.domain.model.NoteShare;
import java.util.List;

public interface NoteShareRepository {
    NoteShare save(NoteShare share);
    List<NoteShare> findByUserId(Long userId);
}
