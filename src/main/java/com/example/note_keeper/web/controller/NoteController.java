package com.example.note_keeper.web.controller;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.application.service.NoteService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/add")
    public ResponseEntity<NoteResponse> addNote(@RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.addNote(request));
    }

    @GetMapping("/{noteId}/versions")
    public ResponseEntity<List<NoteVersionResponse>> getVersions(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.getVersionsByNoteId(noteId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id,
            @RequestBody UpdateNoteRequest request) {
        request.setId(id);
        return ResponseEntity.ok(noteService.updateNote(request));
    }

    @GetMapping("/{userId}/notes")
    public ResponseEntity<List<NoteResponse>> getNotesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(noteService.getNotesByUserId(userId));
    }

    @PutMapping("/{noteId}/restore/{versionId}")
    public ResponseEntity<NoteResponse> restoreVersion(@PathVariable Long noteId,
            @PathVariable Long versionId) {
        return ResponseEntity.ok(noteService.restoreVersion(noteId, versionId));
    }

}
