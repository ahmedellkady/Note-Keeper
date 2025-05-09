package com.example.note_keeper.application.dto;

import com.example.note_keeper.domain.model.NoteTag;

public class UpdateNoteRequest {
    private Long id;
    private String title;
    private String content;
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
    public NoteTag getTag() {
        return tag;
    }
    public void setTag(NoteTag tag) {
        this.tag = tag;
    }

    
}
