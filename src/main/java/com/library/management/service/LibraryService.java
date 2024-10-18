package com.library.management.service;

import com.library.management.exception.NoAvailableCopiesException;
import com.library.management.exception.ReturnCopiesNotAllowedException;
import com.library.management.mapper.BookMapper;
import com.library.management.model.Book;
import com.library.management.model.BookBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryService {

    private final BookService bookService;
    private final CacheService cacheService;
    private final BookMapper bookMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized void addBook(final Book book) {
        BookBean bookBean = bookMapper.toBean(book);
        bookService.addBook(bookBean);
    }

    public List<Book> findBooksByAuthor(String author) {
        List<BookBean> booksByAuthor = cacheService.findBooksByAuthor(author);
        return booksByAuthor.stream().map(bookMapper::toPojo).collect(Collectors.toList());
    }

    public Book findBookByISBN(String isbn) {
        BookBean bookBean = bookService.findBookById(isbn);
        return bookMapper.toPojo(bookBean);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Book borrowBook(String isbn) {
        BookBean bookBean = bookService.findBookById(isbn);
        if (bookBean.getAvailableCopies() == 0) {
            throw new NoAvailableCopiesException(isbn);
        }
        bookBean.setAvailableCopies(bookBean.getAvailableCopies() - 1);
        bookService.save(bookBean);
        return bookMapper.toPojo(bookBean);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Book returnBook(String isbn) {
        BookBean bookBean = bookService.findBookById(isbn);
        if (bookBean.getAvailableCopies() < bookBean.getInitialCopies()) {
            bookBean.setAvailableCopies(bookBean.getAvailableCopies() + 1);
            bookService.save(bookBean);
            return bookMapper.toPojo(bookBean);
        }
        throw new ReturnCopiesNotAllowedException(isbn);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized void removeBook(String isbn){
        bookService.removeBook(isbn);
    }


}
