package com.library.management.exception;

public class NoAvailableCopiesException extends RuntimeException {
    public NoAvailableCopiesException(String isbn) {
        super("No available copies exists for book with ISBN " + isbn);
    }
}
