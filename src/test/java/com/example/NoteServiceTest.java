package com.example;

import com.example.dto.NoteBrief;
import com.example.exception.NoteWasNotFoundException;
import com.example.model.Note;
import com.example.model.NoteTag;
import com.example.repository.NoteRepository;
import com.example.service.NoteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note note1;
    private Note note2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        note1 = new Note("Title1", "note is just a note", NoteTag.BUSINESS);
        note1.setCreatedDate(LocalDateTime.now().minusDays(1));
        note1.setId(1L);

        note2 = new Note("Title2", "another test note", NoteTag.PERSONAL);
        note2.setCreatedDate(LocalDateTime.now());
        note2.setId(2L);
    }

    // ==================== getAllNotesBrief ====================
    @Test
    public void testGetAllNotesBrief_ReturnsMappedNotes() {
        List<Note> notes = Arrays.asList(note2, note1); // descending by createdDate
        Page<Note> page = new PageImpl<>(notes);

        when(noteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<NoteBrief> result = noteService.getAllNotesBrief();

        assertEquals(2, result.size());
        assertEquals("Title2", result.get(0).getTitle()); // newest first
        verify(noteRepository, times(1)).findAll(any(PageRequest.class));
    }

    // ==================== getStats ====================
    @Test
    public void testGetStats_ReturnsCorrectWordFrequency() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note1));

        Map<String, Integer> stats = noteService.getStats(1L);

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("note", 2);
        expected.put("is", 1);
        expected.put("just", 1);
        expected.put("a", 1);

        assertEquals(expected, stats);
    }

    @Test
    public void testGetStats_ThrowsException_WhenNoteNotFound() {
        when(noteRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NoteWasNotFoundException.class, () -> noteService.getStats(3L));
    }

    // ==================== getAllNotes (with tag) ====================
    @Test
    public void testGetAllNotes_FilteredByTag() {
        Page<Note> page = new PageImpl<>(Collections.singletonList(note1));
        when(noteRepository.findByTag(eq(NoteTag.BUSINESS), any(PageRequest.class))).thenReturn(page);

        List<Note> result = noteService.getAllNotes(NoteTag.BUSINESS);

        assertEquals(1, result.size());
        assertEquals(NoteTag.BUSINESS, result.get(0).getTag());
    }

    @Test
    public void testGetAllNotes_NoTag_ReturnsAll() {
        Page<Note> page = new PageImpl<>(Arrays.asList(note2, note1));
        when(noteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<Note> result = noteService.getAllNotes(null);

        assertEquals(2, result.size());
        assertEquals(note2.getId(), result.get(0).getId());
    }

    // ==================== getNoteById ====================
    @Test
    public void testGetNoteById_Found() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note1));
        Note result = noteService.getNoteById(1L);
        assertEquals(note1.getId(), result.getId());
    }

    @Test
    public void testGetNoteById_NotFound() {
        when(noteRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoteWasNotFoundException.class, () -> noteService.getNoteById(10L));
    }

    // ==================== createNote ====================
    @Test
    public void testCreateNote_SavesAndReturnsNote() {
        when(noteRepository.save(any(Note.class))).thenReturn(note1);

        Note result = noteService.createNote(note1);

        assertEquals(note1.getId(), result.getId());
        verify(noteRepository, times(1)).save(note1);
    }

    // ==================== updateNote ====================
    @Test
    public void testUpdateNote_UpdatesExistingNote() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note1));
        when(noteRepository.save(any(Note.class))).thenReturn(note1);

        Note updated = new Note("UpdatedTitle", "Updated text", NoteTag.PERSONAL);
        updated.setCreatedDate(LocalDateTime.now());

        Note result = noteService.updateNote(1L, updated);

        assertEquals("UpdatedTitle", result.getTitle());
        assertEquals("Updated text", result.getText());
        assertEquals(NoteTag.PERSONAL, result.getTag());
    }

    @Test
    public void testUpdateNote_ThrowsException_WhenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());
        Note updated = new Note("UpdatedTitle", "Updated text", NoteTag.PERSONAL);

        assertThrows(NoteWasNotFoundException.class, () -> noteService.updateNote(99L, updated));
    }

    // ==================== deleteNote ====================
    @Test
    public void testDeleteNote_CallsRepositoryDelete() {
        doNothing().when(noteRepository).deleteById(1L);
        noteService.deleteNote(1L);
        verify(noteRepository, times(1)).deleteById(1L);
    }
}
