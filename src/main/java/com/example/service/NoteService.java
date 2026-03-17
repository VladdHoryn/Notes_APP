package com.example.service;

import com.example.dto.NoteBrief;
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

    public List<NoteBrief> getAllNotesBrief(){
        return noteRepository.findAll().stream()
                .map(note -> NoteBrief.mapFromNote(note))
                .toList();
    }

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

    @Transactional
    public Note updateNote(Long noteId, Note note){
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteWasNotFoundException(noteId));

        existingNote.setTitle(note.getTitle());
        existingNote.setCreatedDate(note.getCreatedDate());
        existingNote.setText(note.getText());
        existingNote.setTag(note.getTag());

        return noteRepository.save(existingNote);
    }

    @Transactional
    public void deleteNote(Long id){
        noteRepository.deleteById(id);
    }
}
