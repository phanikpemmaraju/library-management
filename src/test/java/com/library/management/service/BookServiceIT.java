package com.library.management.service;

import com.library.management.model.BookBean;
import com.library.management.repository.BookRepository;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"integration"})
public class BookServiceIT {

    private static final BookBean BOOK_BEAN = TestDataUtils.getDefaultBookBean();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void addBookSuccess() {
        Optional<BookBean> bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertFalse(bookBean.isPresent());

        bookService.addBook(BOOK_BEAN);
        bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertTrue(bookBean.isPresent());
        assertEquals(BOOK_BEAN, bookBean.get());
    }

    @Test
    void bookByIdSuccess() {
        bookRepository.save(BOOK_BEAN);
        Optional<BookBean> bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertTrue(bookBean.isPresent());

        BookBean bookByISBN = bookService.findBookById(TestDataUtils.ISBN);

        assertEquals(BOOK_BEAN, bookByISBN);
    }

    @Test
    void findBooksByValidAuthorSuccess() {
        bookRepository.save(BOOK_BEAN);
        List<BookBean> expected = bookRepository.findByAuthor(TestDataUtils.AUTHOR);
        assertEquals(1, expected.size());

        List<BookBean> actual = bookService.findBooksByAuthor(TestDataUtils.AUTHOR);
        assertEquals(expected, actual);
    }

    @Test
    void findBooksByInvalidAuthorSuccess() {
        bookRepository.save(BOOK_BEAN);

        List<BookBean> actual = bookService.findBooksByAuthor("test");
        assertEquals(0, actual.size());
    }

    @Test
    void saveBookSuccess() {
        Optional<BookBean> bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertFalse(bookBean.isPresent());

        bookService.save(BOOK_BEAN);
        bookBean = bookRepository.findById(TestDataUtils.ISBN);

        assertEquals(BOOK_BEAN, bookBean.get());
    }

    @Test
    void removeBookSuccess(){
        bookRepository.save(BOOK_BEAN);
        Optional<BookBean> bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertTrue(bookBean.isPresent());

        bookService.removeBook(TestDataUtils.ISBN);
        bookBean = bookRepository.findById(TestDataUtils.ISBN);
        assertFalse(bookBean.isPresent());
    }



}
