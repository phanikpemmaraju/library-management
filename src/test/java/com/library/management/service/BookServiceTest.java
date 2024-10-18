package com.library.management.service;

import com.library.management.exception.BookAlreadyExistsException;
import com.library.management.exception.BookNotFoundException;
import com.library.management.model.BookBean;
import com.library.management.repository.BookRepository;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final BookBean BOOK_BEAN = TestDataUtils.getDefaultBookBean();

    @Mock
    private BookRepository repository;
    private BookService bookService;

    @BeforeEach
    void setup() {
        bookService = new BookService(repository);
    }

    @Test
    void addBookSuccess() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.empty());
        bookService.addBook(BOOK_BEAN);
        verify(repository).save(BOOK_BEAN);
    }

    @Test
    void addBookIsbnAlreadyExists() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.of(BOOK_BEAN));
        assertThatThrownBy(() -> bookService.addBook(BOOK_BEAN))
                .isExactlyInstanceOf(BookAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("message", "A book with ISBN " + TestDataUtils.ISBN + " already exists.");

        verify(repository, Mockito.never()).save(BOOK_BEAN);
    }

    @Test
    void findBookByIdSuccess() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.of(BOOK_BEAN));
        BookBean bookBean = bookService.findBookById(TestDataUtils.ISBN);
        assertEquals(BOOK_BEAN, bookBean);
    }

    @Test
    void findBookByIdNotFound() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findBookById(TestDataUtils.ISBN))
                .isExactlyInstanceOf(BookNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "A book with ISBN " + TestDataUtils.ISBN + " doesn't exists.");

    }

    @Test
    void removeBookByIsbnSuccess() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.of(BOOK_BEAN));
        doNothing().when(repository).deleteById(TestDataUtils.ISBN);
        bookService.removeBook(TestDataUtils.ISBN);
        verify(repository).deleteById(TestDataUtils.ISBN);
    }

    @Test
    void removeBookByIsbnFailure() {
        when(repository.findById(TestDataUtils.ISBN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.removeBook(TestDataUtils.ISBN))
                .isExactlyInstanceOf(BookNotFoundException.class);
        verify(repository).findById(TestDataUtils.ISBN);
        verify(repository, never()).deleteById(TestDataUtils.ISBN);
    }

    @Test
    void findByAuthorReturnsBooks() {
        List<BookBean> bookBeans = Collections.singletonList(BOOK_BEAN);
        when(repository.findByAuthor(TestDataUtils.AUTHOR)).thenReturn(bookBeans);
        List<BookBean> booksByAuthor = bookService.findBooksByAuthor(TestDataUtils.AUTHOR);
        verify(repository).findByAuthor(TestDataUtils.AUTHOR);
        assertEquals(bookBeans, booksByAuthor);
    }

    @Test
    void findByAuthorReturnsEmptyCollection() {
        List<BookBean> bookBeans = Collections.emptyList();
        when(repository.findByAuthor(TestDataUtils.AUTHOR)).thenReturn(bookBeans);
        List<BookBean> booksByAuthor = bookService.findBooksByAuthor(TestDataUtils.AUTHOR);
        verify(repository).findByAuthor(TestDataUtils.AUTHOR);
        assertEquals(bookBeans, booksByAuthor);
    }

    @Test
    void saveBookSuccess() {
        when(repository.save(BOOK_BEAN)).thenReturn(BOOK_BEAN);
        bookService.save(BOOK_BEAN);
        verify(repository).save(BOOK_BEAN);
    }

}
