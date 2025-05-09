package com.example.note_keeper.infrastructure.persistence.repository;

import com.example.note_keeper.domain.model.NoteShare;
import com.example.note_keeper.domain.repository.NoteShareRepository;
import com.example.note_keeper.infrastructure.persistence.entity.NoteEntity;
import com.example.note_keeper.infrastructure.persistence.entity.NoteShareEntity;
import com.example.note_keeper.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NoteShareJpaRepository implements NoteShareRepository {

    private final NoteShareJpaInterface jpa;
    private final UserJpaInterface jpaUser;

    public NoteShareJpaRepository(NoteShareJpaInterface jpa, UserJpaInterface jpaUser) {
        this.jpa = jpa;
        this.jpaUser = jpaUser;
    }

    @Override
    public NoteShare save(NoteShare share) {
        UserEntity user = jpaUser.findByEmail(share.getSharedWithUserEmail())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with email: " + share.getSharedWithUserEmail()));

        NoteEntity note = new NoteEntity();
        note.setId(share.getNoteId());

        Optional<NoteShareEntity> existingShareOpt = jpa.findByNote_IdAndSharedWithUser_Email(
                share.getNoteId(), share.getSharedWithUserEmail());

        NoteShareEntity entity = existingShareOpt.orElse(new NoteShareEntity());

        entity.setNote(note);
        entity.setSharedWithUser(user);
        entity.setPermission(share.getPermission());

        NoteShareEntity saved = jpa.save(entity);
        share.setId(saved.getId());

        return share;
    }


    @Override
    public List<NoteShare> findByUserId(Long userId) {
        return jpa.findBySharedWithUser_Id(userId).stream().map(e -> {
            NoteShare share = new NoteShare();
            share.setId(e.getId());
            share.setNoteId(e.getNote().getId());
            share.setSharedWithUserEmail(e.getSharedWithUser().getEmail());
            share.setPermission(e.getPermission());
            return share;
        }).collect(Collectors.toList());
    }
}
