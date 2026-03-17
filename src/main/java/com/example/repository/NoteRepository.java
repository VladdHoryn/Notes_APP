package com.example.repository;

import com.example.model.Note;
import com.example.model.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAll(Pageable pageable);

    Page<Note> findByTag(NoteTag tag, Pageable pageable);
}
