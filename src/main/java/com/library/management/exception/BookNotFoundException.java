package com.library.management.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("A book with ISBN " + isbn + " doesn't exists.");
    }
}
