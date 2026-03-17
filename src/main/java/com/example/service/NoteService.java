package com.example.service;

import com.example.dto.NoteBrief;
import com.example.model.Note;
import com.example.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.exception.NoteWasNotFoundException;
import java.util.LinkedHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<NoteBrief> getAllNotesBrief(){
        return noteRepository.findAll().stream()
                .map(note -> NoteBrief.mapFromNote(note))
                .toList();
    }

    public Map<String, Integer> getStats(Long id){
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteWasNotFoundException(id));

        String text = note.getText();

        Map<String, Integer> frequency = new HashMap<>();

        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) {
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }

        return frequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
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
