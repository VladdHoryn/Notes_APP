package com.example;

import com.example.controller.NoteController;
import com.example.dto.NoteBrief;
import com.example.model.Note;
import com.example.model.NoteTag;
import com.example.service.NoteService;
import com.example.exception.NoteWasNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    private Note note1;
    private Note note2;
    private NoteBrief brief1;
    private NoteBrief brief2;

    @BeforeEach
    public void setup() {
        note1 = new Note("Title1", "note is just a note", NoteTag.BUSINESS);
        note1.setId(1L);
        note1.setCreatedDate(LocalDateTime.now().minusDays(1));

        note2 = new Note("Title2", "another test note", NoteTag.PERSONAL);
        note2.setId(2L);
        note2.setCreatedDate(LocalDateTime.now());

        brief1 = NoteBrief.mapFromNote(note1);
        brief2 = NoteBrief.mapFromNote(note2);
    }

    // ==================== GET /brief ====================
    @Test
    public void testGetAllNotesBrief() throws Exception {
        when(noteService.getAllNotesBrief()).thenReturn(Arrays.asList(brief2, brief1));

        mockMvc.perform(get("/note/v1/brief"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Title2")));
    }

    // ==================== GET /stats/{id} ====================
    @Test
    public void testGetStats() throws Exception {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("note", 2);
        stats.put("is", 1);

        when(noteService.getStats(1L)).thenReturn(stats);

        mockMvc.perform(get("/note/v1/stats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note", is(2)))
                .andExpect(jsonPath("$.is", is(1)));
    }

    @Test
    public void testGetStats_NotFound() throws Exception {
        when(noteService.getStats(99L)).thenThrow(new NoteWasNotFoundException(99L));

        mockMvc.perform(get("/note/v1/stats/99"))
                .andExpect(status().is4xxClientError());
    }

    // ==================== GET / ====================
    @Test
    public void testGetAllNotes_WithTag() throws Exception {
        when(noteService.getAllNotes(NoteTag.BUSINESS)).thenReturn(Collections.singletonList(note1));

        mockMvc.perform(get("/note/v1").param("tag", "BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].tag", is("BUSINESS")));
    }

    @Test
    public void testGetAllNotes_NoTag() throws Exception {
        when(noteService.getAllNotes(null)).thenReturn(Arrays.asList(note2, note1));

        mockMvc.perform(get("/note/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Title2")));
    }

    // ==================== GET /{id} ====================
    @Test
    public void testGetNoteById() throws Exception {
        when(noteService.getNoteById(1L)).thenReturn(note1);

        mockMvc.perform(get("/note/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Title1")));
    }

    // ==================== DELETE /{id} ====================
    @Test
    public void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote(1L);

        mockMvc.perform(delete("/note/v1/1"))
                .andExpect(status().isOk());
    }
}