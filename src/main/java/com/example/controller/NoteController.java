package com.example.controller;

import com.example.dto.NoteBrief;
import com.example.model.Note;
import com.example.model.NoteTag;
import com.example.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/note/v1/")
@AllArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/brief")
    public List<NoteBrief> getAllNotesBrief(){
        return noteService.getAllNotesBrief();
    }
    @GetMapping("/stats/{id}")
    public Map<String, Integer> getStats(@PathVariable Long id){
        return noteService.getStats(id);
    }

    @GetMapping
    public List<Note> getAllNotes(@RequestParam(required = false) NoteTag tag){

        return noteService.getAllNotes(tag);
    }
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id){
        return noteService.getNoteById(id);
    }
    @PostMapping
    public Note createNote(@Validated @RequestBody Note note){
        return noteService.createNote(note);
    }
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id,@Validated @RequestBody Note note){
        return noteService.updateNote(id, note);
    }
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id){
        noteService.deleteNote(id);
    }
}
