package com.example.note_keeper.application.service;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.domain.model.Note;
import com.example.note_keeper.domain.model.NoteVersion;
import com.example.note_keeper.domain.repository.NoteRepository;
import com.example.note_keeper.domain.repository.NoteVersionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteVersionRepository noteVersionRepository;

    public NoteService(NoteRepository noteRepository, NoteVersionRepository noteVersionRepository) {
        this.noteRepository = noteRepository;
        this.noteVersionRepository = noteVersionRepository;
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

        NoteVersion version = new NoteVersion();
        version.setNoteId(saved.getId());
        version.setTitle(saved.getTitle());
        version.setContent(saved.getContent());
        version.setCreatedAt(saved.getCreatedAt());
        noteVersionRepository.save(version);

        return new NoteResponse(
                saved.getId(), saved.getTitle(), saved.getContent(),
                saved.getCreatedAt(), saved.getUpdatedAt(), saved.getTag());
    }

    public List<NoteVersionResponse> getVersionsByNoteId(Long noteId) {
        return noteVersionRepository.findByNoteId(noteId).stream()
                .map(v -> new NoteVersionResponse(v.getId(), v.getTitle(), v.getContent(), v.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public NoteResponse updateNote(UpdateNoteRequest request) {
        Note existing = noteRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        NoteVersion version = new NoteVersion();
        version.setNoteId(existing.getId());
        version.setTitle(existing.getTitle());
        version.setContent(existing.getContent());
        version.setCreatedAt(LocalDateTime.now());
        noteVersionRepository.save(version);

        existing.setTitle(request.getTitle());
        existing.setContent(request.getContent());
        existing.setTag(request.getTag());
        existing.setUpdatedAt(LocalDateTime.now());

        Note updated = noteRepository.save(existing);

        return new NoteResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getContent(),
                updated.getCreatedAt(),
                updated.getUpdatedAt(),
                updated.getTag());
    }

}
