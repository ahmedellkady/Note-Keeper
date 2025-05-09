package com.example.note_keeper.infrastructure.persistence.entity;

import com.example.note_keeper.domain.model.NoteSharePermission;
import jakarta.persistence.*;

@Entity
@Table(name = "note_shares")
public class NoteShareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private NoteEntity note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id")
    private UserEntity sharedWithUser;

    @Enumerated(EnumType.STRING)
    private NoteSharePermission permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoteEntity getNote() {
        return note;
    }

    public void setNote(NoteEntity note) {
        this.note = note;
    }

    public UserEntity getSharedWithUser() {
        return sharedWithUser;
    }

    public void setSharedWithUser(UserEntity sharedWithUser) {
        this.sharedWithUser = sharedWithUser;
    }

    public NoteSharePermission getPermission() {
        return permission;
    }

    public void setPermission(NoteSharePermission permission) {
        this.permission = permission;
    }

    
}
