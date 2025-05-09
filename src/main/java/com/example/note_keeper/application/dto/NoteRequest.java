package com.example.note_keeper.application.dto;

import com.example.note_keeper.domain.model.NoteTag;

public class NoteRequest {
    private String title;
    private String content;
    private Long userId;
    private NoteTag tag;
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
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public NoteTag getTag() {
        return tag;
    }
    public void getTag(NoteTag tag) {
        this.tag = tag;
    }

    
}
