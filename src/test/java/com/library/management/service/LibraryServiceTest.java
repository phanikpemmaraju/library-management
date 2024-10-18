package com.library.management.service;

import com.library.management.exception.NoAvailableCopiesException;
import com.library.management.exception.ReturnCopiesNotAllowedException;
import com.library.management.mapper.BookMapper;
import com.library.management.mapper.BookMapperImpl;
import com.library.management.model.Book;
import com.library.management.model.BookBean;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    private static final Book BOOK = TestDataUtils.getDefaultBook();
    private BookBean BOOK_BEAN;

    @Mock
    private BookService bookService;
    @Mock
    private CacheService cacheService;
    private BookMapper bookMapper;
    private LibraryService libraryService;

    @BeforeEach
    void init() {
        BOOK_BEAN = TestDataUtils.getDefaultBookBean();
        bookMapper = new BookMapperImpl();
        libraryService = new LibraryService(bookService, cacheService, bookMapper);
    }

    @Test
    public void addBookSuccess() {
        libraryService.addBook(BOOK);
        verify(bookService).addBook(BOOK_BEAN);
    }

    @Test
    public void testFindBooksByAuthor() {
        List<BookBean> bookBeans = Collections.singletonList(BOOK_BEAN);
        when(cacheService.findBooksByAuthor(TestDataUtils.AUTHOR)).thenReturn(bookBeans);
        List<Book> result = libraryService.findBooksByAuthor(TestDataUtils.AUTHOR);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cacheService).findBooksByAuthor(TestDataUtils.AUTHOR);
    }

    @Test
    public void testFindBookByISBN() {
        when(bookService.findBookById(TestDataUtils.ISBN)).thenReturn(BOOK_BEAN);

        Book foundBook = libraryService.findBookByISBN(TestDataUtils.ISBN);

        assertNotNull(foundBook);
        assertEquals(TestDataUtils.ISBN, foundBook.getIsbn());
        verify(bookService).findBookById(TestDataUtils.ISBN);
    }

    @Test
    public void testBorrowBook_Success() {
        when(bookService.findBookById(TestDataUtils.ISBN)).thenReturn(BOOK_BEAN);
        Book borrowedBook = libraryService.borrowBook(TestDataUtils.ISBN);

        assertNotNull(borrowedBook);
        verify(bookService).findBookById(TestDataUtils.ISBN);
        verify(bookService).save(BOOK_BEAN);
        assertEquals(1, BOOK_BEAN.getAvailableCopies());
    }

    @Test
    public void testBorrowBook_NoCopies() {
        // Given
        BOOK_BEAN.setAvailableCopies(0);
        when(bookService.findBookById(TestDataUtils.ISBN)).thenReturn(BOOK_BEAN);

        // Then
        assertThatThrownBy(() -> libraryService.borrowBook(TestDataUtils.ISBN))
                .isExactlyInstanceOf(NoAvailableCopiesException.class);
        verify(bookService).findBookById(TestDataUtils.ISBN);
        verify(bookService, never()).save(BOOK_BEAN);
    }


    @Test
    public void testReturnBook_Success() {
        BOOK_BEAN.setAvailableCopies(1);
        when(bookService.findBookById(TestDataUtils.ISBN)).thenReturn(BOOK_BEAN);

        Book returnedBook = libraryService.returnBook(TestDataUtils.ISBN);

        assertNotNull(returnedBook);
        verify(bookService).findBookById(TestDataUtils.ISBN);
        verify(bookService).save(BOOK_BEAN);
        assertEquals(2, BOOK_BEAN.getAvailableCopies());
    }

    @Test
    public void testReturnBook_ExceedsInitialCopies() {
        BOOK_BEAN.setAvailableCopies(10);
        when(bookService.findBookById(TestDataUtils.ISBN)).thenReturn(BOOK_BEAN);

        assertThatThrownBy(() -> libraryService.returnBook(TestDataUtils.ISBN))
                .isExactlyInstanceOf(ReturnCopiesNotAllowedException.class);
        verify(bookService, times(1)).findBookById(TestDataUtils.ISBN);
        verify(bookService, never()).save(BOOK_BEAN);
    }

    @Test
    public void testRemoveBook() {
        String isbn = "123";
        libraryService.removeBook(isbn);
        verify(bookService).removeBook(isbn);
    }

}
