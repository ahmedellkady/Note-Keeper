package com.example.note_keeper.web.controller;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.application.service.NoteService;
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
}
