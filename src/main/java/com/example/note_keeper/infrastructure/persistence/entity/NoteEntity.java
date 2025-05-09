package com.example.note_keeper.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.note_keeper.domain.model.NoteTag;

@Entity
@Table(name = "notes")
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private NoteTag tag;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public NoteTag getTag() {
        return tag;
    }
    public void setTag(NoteTag tag) {
        this.tag = tag;
    }

    
}
