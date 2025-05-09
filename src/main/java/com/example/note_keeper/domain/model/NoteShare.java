package com.example.note_keeper.domain.model;

public class NoteShare {
    private Long id;
    private Long noteId;
    private String sharedWithUserEmail;
    private NoteSharePermission permission;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
