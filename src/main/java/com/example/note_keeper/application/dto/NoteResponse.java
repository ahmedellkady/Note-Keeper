package com.example.note_keeper.application.dto;

import java.time.LocalDateTime;

import com.example.note_keeper.domain.model.NoteTag;

public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private NoteTag tag;

    public NoteResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, NoteTag tag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public NoteTag getTag() {
        return tag;
    }
    
    
}
