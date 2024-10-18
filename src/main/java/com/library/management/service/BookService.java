package com.library.management.service;

import com.library.management.exception.BookAlreadyExistsException;
import com.library.management.exception.BookNotFoundException;
import com.library.management.model.BookBean;
import com.library.management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public void addBook(final BookBean bookBean) {
        bookRepository.findById(bookBean.getIsbn())
                .ifPresent(bean -> { throw new BookAlreadyExistsException(bookBean.getIsbn());});

        bookRepository.save(bookBean);
    }

    public BookBean findBookById(final String isbn) {
        return bookRepository.findById(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public void removeBook(final String isbn) {
        bookRepository.deleteById(findBookById(isbn).getIsbn());
    }

    public List<BookBean> findBooksByAuthor(final String author) {
        return bookRepository.findByAuthor(author);
    }

    public void save(final BookBean bookBean) {
        bookRepository.save(bookBean);
    }

}
