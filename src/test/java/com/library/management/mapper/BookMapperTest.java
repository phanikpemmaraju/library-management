package com.library.management.mapper;

import com.library.management.model.Book;
import com.library.management.model.BookBean;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.library.management.util.TestDataUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookMapperTest {

    private static final Book BOOK = TestDataUtils.getDefaultBook();
    private static final BookBean BOOK_BEAN = TestDataUtils.getDefaultBookBean();

    private BookMapper mapper;

    @BeforeEach
    void init() {
        mapper = new BookMapperImpl();
    }

    @Test
    void shouldReturnNullFromBean() {
        assertNull(mapper.toBean(null));
    }

    @Test
    void shouldReturnNullFromPojo() {
        assertNull(mapper.toPojo(null));
    }

    @Test
    void shouldMapToBookBean() {
        BookBean bean = mapper.toBean(BOOK);
        assertEquals(ISBN, bean.getIsbn());
        assertEquals(TITLE, bean.getTitle());
        assertEquals(AUTHOR, bean.getAuthor());
        assertEquals(PUBLISHED_YEAR, bean.getPublicationYear());
        assertEquals(AVAILABLE_COPIES, bean.getAvailableCopies());
        assertEquals(AVAILABLE_COPIES, bean.getInitialCopies());
    }

    @Test
    void shouldMapToBookPojo() {
        Book book = mapper.toPojo(BOOK_BEAN);
        assertEquals(ISBN, book.getIsbn());
        assertEquals(TITLE, book.getTitle());
        assertEquals(AUTHOR, book.getAuthor());
        assertEquals(PUBLISHED_YEAR, book.getPublicationYear());
        assertEquals(AVAILABLE_COPIES, book.getAvailableCopies());
    }
}
