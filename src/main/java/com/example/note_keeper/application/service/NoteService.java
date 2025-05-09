package com.example.note_keeper.application.service;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.domain.model.Note;
import com.example.note_keeper.domain.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse addNote(NoteRequest request) {
        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setUserId(request.getUserId());
        note.setTag(request.getTag());

        Note saved = noteRepository.save(note);

        return new NoteResponse(
            saved.getId(), saved.getTitle(), saved.getContent(),
            saved.getCreatedAt(), saved.getUpdatedAt() , saved.getTag()
        );
    }
}
