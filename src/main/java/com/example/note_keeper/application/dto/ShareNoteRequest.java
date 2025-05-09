package com.example.note_keeper.application.dto;

import com.example.note_keeper.domain.model.NoteSharePermission;

public class ShareNoteRequest {
    private Long noteId;
    private String sharedWithUserEmail;
    private NoteSharePermission permission;
    public Long getNoteId() {
        return noteId;
    }
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
    public String getSharedWithUserEmail() {
        return sharedWithUserEmail;
    }
    public void setSharedWithUserEmail(String sharedWithUserEmail) {
        this.sharedWithUserEmail = sharedWithUserEmail;
    }
    public NoteSharePermission getPermission() {
        return permission;
    }
    public void setPermission(NoteSharePermission permission) {
        this.permission = permission;
    } 

    
}
