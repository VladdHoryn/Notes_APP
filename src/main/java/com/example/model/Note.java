package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title can not be empty")
    private String title;
    private LocalDateTime createdDate;
    @NotBlank(message = "Text can not be empty")
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
