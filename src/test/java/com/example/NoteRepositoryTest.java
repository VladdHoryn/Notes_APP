package com.example;

import com.example.model.Note;
import com.example.model.NoteTag;
import com.example.repository.NoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class NoteRepositoryTest {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteRepositoryTest(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    @Test
    @DisplayName("Should save a note successfully")
    void testSaveNote(){
        Note note = new Note("Test Title", "Test text", NoteTag.PERSONAL);
        Note savedNote = noteRepository.save(note);

        Assertions.assertThat(savedNote.getId()).isNotNull();
        Assertions.assertThat(savedNote.getTitle()).isEqualTo("Test Title");
        Assertions.assertThat(savedNote.getText()).isEqualTo("Test text");
        Assertions.assertThat(savedNote.getTag()).isEqualTo(NoteTag.PERSONAL);
    }

    @Test
    @DisplayName("Should find a note by id")
    void testFindById() {
        Note note = new Note("Title 1", "Some text");
        Note savedNote = noteRepository.save(note);

        Optional<Note> foundNote = noteRepository.findById(savedNote.getId());
        Assertions.assertThat(foundNote).isPresent();
        Assertions.assertThat(foundNote.get().getTitle()).isEqualTo("Title 1");
    }

    @Test
    @DisplayName("Should find all notes")
    void testFindAll() {
        noteRepository.save(new Note("Title 1", "Text 1"));
        noteRepository.save(new Note("Title 2", "Text 2"));

        List<Note> notes = noteRepository.findAll();
        Assertions.assertThat(notes).hasSize(2);
    }

    @Test
    @DisplayName("Should delete a note")
    void testDeleteNote() {
        Note note = new Note("To delete", "Text");
        Note savedNote = noteRepository.save(note);

        noteRepository.delete(savedNote);

        Optional<Note> deletedNote = noteRepository.findById(savedNote.getId());
        Assertions.assertThat(deletedNote).isEmpty();
    }
}
