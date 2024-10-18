package com.library.management.service;

import com.library.management.model.BookBean;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CacheServiceTest {

    private static final BookBean BOOK_BEAN = TestDataUtils.getDefaultBookBean();
    @Mock
    private BookService bookService;

    private CacheService cacheService;

    @BeforeEach
    void init() {
        cacheService = new CacheService(bookService);
    }

    @Test
    void findBooksByAuthorCache() {
        List<BookBean> bookBeans = Collections.singletonList(BOOK_BEAN);
        Mockito.when(bookService.findBooksByAuthor(TestDataUtils.AUTHOR)).thenReturn(bookBeans);
        List<BookBean> booksByAuthor = cacheService.findBooksByAuthor(TestDataUtils.AUTHOR);
        assertEquals(bookBeans, booksByAuthor);
    }
}
