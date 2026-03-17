package com.example.dto;

import com.example.model.Note;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoteBrief {
    @NotBlank(message = "Title can not be empty")
    private String title;
    private LocalDateTime createdDate;

    public static NoteBrief mapFromNote(Note note){
        NoteBrief noteBrief = new NoteBrief(note.getTitle(), note.getCreatedDate());

        return noteBrief;
    }
}
