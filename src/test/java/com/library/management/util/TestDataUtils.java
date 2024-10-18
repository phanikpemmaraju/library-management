package com.library.management.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.model.Book;
import com.library.management.model.BookBean;

import java.util.UUID;

public class TestDataUtils {

    public static final String ISBN = UUID.randomUUID().toString();
    public static final String TITLE = "Treasure Island";
    public static final String AUTHOR = "Robert Louis Stevenson";
    public static final int AVAILABLE_COPIES = 2;
    public static final int PUBLISHED_YEAR = 2020;

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static Book getDefaultBook() {
        return Book.builder()
                .isbn(ISBN)
                .title(TITLE)
                .author(AUTHOR)
                .publicationYear(PUBLISHED_YEAR)
                .availableCopies(AVAILABLE_COPIES)
                .build();
    }

    public static BookBean getDefaultBookBean() {
        return new BookBean(ISBN, TITLE, AUTHOR, PUBLISHED_YEAR, AVAILABLE_COPIES, AVAILABLE_COPIES);
    }
}
