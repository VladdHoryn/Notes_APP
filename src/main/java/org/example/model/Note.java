package org.example.model;

import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.aot.generate.Generated;

import java.time.LocalDateTime;

@Entity
@Table(schema = "notes")
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime createdDate;
    private String text;
    @Enumerated(EnumType.STRING)
    private NoteTag tag;

    public Note(String title, String text){
        this.title = title;
        this.createdDate = LocalDateTime.now();
        this.text = text;
    }
    public Note(String title, String text, NoteTag tag){
        this.title = title;
        this.createdDate = LocalDateTime.now();
        this.text = text;
        this.tag = tag;
    }
}
