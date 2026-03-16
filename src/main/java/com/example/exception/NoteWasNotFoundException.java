package com.example.exception;

public class NoteWasNotFoundException extends RuntimeException {
    public NoteWasNotFoundException(Long id) {
        super("Note was not found with id: " + id);
    }
}
