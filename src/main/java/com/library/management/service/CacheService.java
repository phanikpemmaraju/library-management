package com.library.management.service;

import com.library.management.model.BookBean;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final BookService bookService;
    @Cacheable(value = "books", key = "#author")
    public List<BookBean> findBooksByAuthor(String author) {
        return bookService.findBooksByAuthor(author);
    }

}
