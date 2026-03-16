package com.example.service;

import com.example.model.Note;
import com.example.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.exception.NoteWasNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id){
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteWasNotFoundException(id));
    }

    @Transactional
    public Note createNote(Note note){
        return noteRepository.save(note);
    }
}
