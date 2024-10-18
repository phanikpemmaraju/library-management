package com.library.management.exception;

public class ReturnCopiesNotAllowedException extends RuntimeException {
    public ReturnCopiesNotAllowedException(String isbn) {
        super("A book with ISBN " + isbn + " has exceeded the return copies.");
    }
}
