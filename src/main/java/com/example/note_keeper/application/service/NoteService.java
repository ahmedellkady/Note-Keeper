package com.example.note_keeper.application.service;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.domain.model.Note;
import com.example.note_keeper.domain.model.NoteShare;
import com.example.note_keeper.domain.model.NoteVersion;
import com.example.note_keeper.domain.model.User;
import com.example.note_keeper.domain.repository.NoteRepository;
import com.example.note_keeper.domain.repository.NoteShareRepository;
import com.example.note_keeper.domain.repository.NoteVersionRepository;
import com.example.note_keeper.domain.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteVersionRepository noteVersionRepository;
    private final NoteShareRepository noteShareRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, NoteVersionRepository noteVersionRepository, NoteShareRepository noteShareRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.noteVersionRepository = noteVersionRepository;
        this.noteShareRepository = noteShareRepository;
        this.userRepository = userRepository;
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
    
    private String getUserName(Long userId) {
        return userRepository.findById(userId)
                .map(User::getName)
                .orElse("Unknown");
    }

    public List<NoteResponse> getNotesByUserId(Long userId) {
        List<Note> ownedNotes = noteRepository.findByUserId(userId);
        List<NoteShare> sharedLinks = noteShareRepository.findByUserId(userId);
        List<NoteResponse> responses = new ArrayList<>();

        for (Note note : ownedNotes) {
                responses.add(new NoteResponse(
                                note.getId(),
                                note.getTitle(),
                                note.getContent(),
                                "OWNER",
                                getUserName(note.getUserId()),
                                note.getCreatedAt(),
                                note.getUpdatedAt(),
                                note.getTag()
                                ));
        }
        
        for (NoteShare share : sharedLinks) {
                noteRepository.findById(share.getNoteId()).ifPresent(note -> {
                        responses.add(new NoteResponse(
                                        note.getId(),
                                        note.getTitle(),
                                        note.getContent(),
                                        share.getPermission().name(),
                                        getUserName(note.getUserId()),
                                        note.getCreatedAt(),
                                        note.getUpdatedAt(),
                                        note.getTag()));
                });
        }
        
        return responses;
    }

    public NoteResponse restoreVersion(Long noteId, Long versionId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        NoteVersion version = noteVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));

        NoteVersion current = new NoteVersion();
        current.setNoteId(note.getId());
        current.setTitle(note.getTitle());
        current.setContent(note.getContent());
        current.setCreatedAt(LocalDateTime.now());
        noteVersionRepository.save(current);

        note.setTitle(version.getTitle());
        note.setContent(version.getContent());
        note.setUpdatedAt(LocalDateTime.now());

        Note updated = noteRepository.save(note);

        return new NoteResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getContent(),
                updated.getCreatedAt(),
                updated.getUpdatedAt(),
                updated.getTag());
    }

    public void shareNote(ShareNoteRequest request) {
        NoteShare share = new NoteShare();
        share.setNoteId(request.getNoteId());
        share.setSharedWithUserEmail(request.getSharedWithUserEmail());
        share.setPermission(request.getPermission());
        noteShareRepository.save(share);
    }

}
