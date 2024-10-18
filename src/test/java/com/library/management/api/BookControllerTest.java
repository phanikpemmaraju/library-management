package com.library.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.management.exception.*;
import com.library.management.model.Book;
import com.library.management.service.LibraryService;
import com.library.management.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.Collections;

import static com.library.management.util.TestDataUtils.objectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private static final Book BOOK = TestDataUtils.getDefaultBook();
    private static final String BOOKS_URI = "/books";

    @Mock
    private LibraryService libraryService;

    private MockMvc mockMvc;
    private String payload;

    @BeforeEach
    void setup() throws JsonProcessingException {
        payload = objectMapper.writeValueAsString(BOOK);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        BookController bookController = new BookController(libraryService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).setControllerAdvice(globalExceptionHandler).build();
    }

    @Nested
    class AddBook {
        @Test
        void addBookSuccess() throws Exception {
            doNothing().when(libraryService).addBook(BOOK);

            mockMvc.perform(post(BOOKS_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.isbn").value(TestDataUtils.ISBN))
                    .andExpect(jsonPath("$.title").value(TestDataUtils.TITLE))
                    .andExpect(jsonPath("$.author").value(TestDataUtils.AUTHOR))
                    .andExpect(jsonPath("$.publicationYear").value(TestDataUtils.PUBLISHED_YEAR))
                    .andExpect(jsonPath("$.availableCopies").value(TestDataUtils.AVAILABLE_COPIES));

            Mockito.verify(libraryService).addBook(BOOK);
        }

        @Test
        void addBookBadRequest() throws Exception {
            Book book = Book.builder().build();
            String payload = objectMapper.writeValueAsString(book);

            mockMvc.perform(post(BOOKS_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(libraryService);
        }

        @Test
        void addBookFailure() throws Exception {
            Mockito.doThrow(BookAlreadyExistsException.class).when(libraryService).addBook(BOOK);

            mockMvc.perform(post(BOOKS_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isConflict());

            Mockito.verify(libraryService).addBook(BOOK);
        }
    }

    @Nested
    class RemoveBook {

        @Test
        void removeBookSuccess() throws Exception {
            doNothing().when(libraryService).removeBook(TestDataUtils.ISBN);
            mockMvc.perform(delete(BOOKS_URI + File.separator + TestDataUtils.ISBN))
                    .andExpect(status().isOk());
            verify(libraryService).removeBook(TestDataUtils.ISBN);
        }

        @Test
        void removeBookBadRequest() throws Exception {
            mockMvc.perform(delete(BOOKS_URI + File.separator + " "))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(libraryService);
        }

        @Test
        void removeBookFailure() throws Exception {
            doThrow(BookNotFoundException.class).when(libraryService).removeBook(TestDataUtils.ISBN);
            mockMvc.perform(delete(BOOKS_URI + File.separator + TestDataUtils.ISBN))
                    .andExpect(status().isNotFound());
            verify(libraryService).removeBook(TestDataUtils.ISBN);
        }
    }

    @Nested
    class ISBNBook {

        @Test
        void findBookByISBNSuccess() throws Exception {
            when(libraryService.findBookByISBN(TestDataUtils.ISBN)).thenReturn(BOOK);
            mockMvc.perform(get(BOOKS_URI+File.separator+TestDataUtils.ISBN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isbn").value(TestDataUtils.ISBN))
                    .andExpect(jsonPath("$.title").value(TestDataUtils.TITLE))
                    .andExpect(jsonPath("$.author").value(TestDataUtils.AUTHOR))
                    .andExpect(jsonPath("$.publicationYear").value(TestDataUtils.PUBLISHED_YEAR))
                    .andExpect(jsonPath("$.availableCopies").value(TestDataUtils.AVAILABLE_COPIES));

            verify(libraryService).findBookByISBN(TestDataUtils.ISBN);
        }

        @Test
        void findBookByISBNBadRequest() throws Exception {
            mockMvc.perform(get(BOOKS_URI+File.separator+ " "))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(libraryService);
        }

        @Test
        void findBookByInvalidISBN() throws Exception {
            doThrow(BookNotFoundException.class).when(libraryService).findBookByISBN(TestDataUtils.ISBN);
            mockMvc.perform(get(BOOKS_URI+File.separator+TestDataUtils.ISBN))
                    .andExpect(status().isNotFound());

            verify(libraryService).findBookByISBN(TestDataUtils.ISBN);
        }
    }

    @Nested
    class BookByAuthor {

        @Test
        void findBookByAuthorSuccess() throws Exception {
            when(libraryService.findBooksByAuthor(TestDataUtils.AUTHOR)).thenReturn(Collections.singletonList(BOOK));

            mockMvc.perform(get(BOOKS_URI+File.separator+"author"+ File.separator+TestDataUtils.AUTHOR))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].isbn").value(TestDataUtils.ISBN))
                    .andExpect(jsonPath("$[0].title").value(TestDataUtils.TITLE))
                    .andExpect(jsonPath("$[0].author").value(TestDataUtils.AUTHOR))
                    .andExpect(jsonPath("$[0].publicationYear").value(TestDataUtils.PUBLISHED_YEAR))
                    .andExpect(jsonPath("$[0].availableCopies").value(TestDataUtils.AVAILABLE_COPIES));

            verify(libraryService).findBooksByAuthor(TestDataUtils.AUTHOR);
        }

        @Test
        void findBookByAuthorBadRequest() throws Exception {
            mockMvc.perform(get(BOOKS_URI+File.separator+"author"+ File.separator+" "))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(libraryService);
        }

        @Test
        void findBookByNonExistentAuthor() throws Exception {
            when(libraryService.findBooksByAuthor(TestDataUtils.AUTHOR)).thenReturn(Collections.emptyList());

            mockMvc.perform(get(BOOKS_URI+File.separator+"author"+ File.separator+TestDataUtils.AUTHOR))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

            verify(libraryService).findBooksByAuthor(TestDataUtils.AUTHOR);
        }
    }

    @Nested
    class BorrowBook {

        @Test
        void borrowBookByISBNSuccess() throws Exception {
            when(libraryService.borrowBook(TestDataUtils.ISBN)).thenReturn(BOOK);
            mockMvc.perform(put(BOOKS_URI+File.separator+"borrow" + File.separator + TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isbn").value(TestDataUtils.ISBN))
                    .andExpect(jsonPath("$.title").value(TestDataUtils.TITLE))
                    .andExpect(jsonPath("$.author").value(TestDataUtils.AUTHOR))
                    .andExpect(jsonPath("$.publicationYear").value(TestDataUtils.PUBLISHED_YEAR))
                    .andExpect(jsonPath("$.availableCopies").value(TestDataUtils.AVAILABLE_COPIES));

            verify(libraryService).borrowBook(TestDataUtils.ISBN);
        }

        @Test
        void borrowBookByISBNBadRequest() throws Exception {
            mockMvc.perform(put(BOOKS_URI+File.separator+"borrow" + File.separator + " ")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(libraryService);
        }

        @Test
        void borrowBookByInvalidISBN() throws Exception {
            doThrow(BookNotFoundException.class).when(libraryService).borrowBook(TestDataUtils.ISBN);
            mockMvc.perform(put(BOOKS_URI+File.separator+"borrow" + File.separator +TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(libraryService).borrowBook(TestDataUtils.ISBN);
        }

        @Test
        void borrowBookByISBNWithNoAvailableCopies() throws Exception {
            doThrow(NoAvailableCopiesException.class).when(libraryService).borrowBook(TestDataUtils.ISBN);
            mockMvc.perform(put(BOOKS_URI+File.separator+"borrow" + File.separator +TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(libraryService).borrowBook(TestDataUtils.ISBN);
        }

    }

    @Nested
    class ReturnBook {

        @Test
        void returnBookByISBNSuccess() throws Exception {
            when(libraryService.returnBook(TestDataUtils.ISBN)).thenReturn(BOOK);
            mockMvc.perform(put(BOOKS_URI+File.separator+"return" + File.separator + TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isbn").value(TestDataUtils.ISBN))
                    .andExpect(jsonPath("$.title").value(TestDataUtils.TITLE))
                    .andExpect(jsonPath("$.author").value(TestDataUtils.AUTHOR))
                    .andExpect(jsonPath("$.publicationYear").value(TestDataUtils.PUBLISHED_YEAR))
                    .andExpect(jsonPath("$.availableCopies").value(TestDataUtils.AVAILABLE_COPIES));

            verify(libraryService).returnBook(TestDataUtils.ISBN);
        }

        @Test
        void returnBookByISBNBadRequest() throws Exception {
            mockMvc.perform(put(BOOKS_URI+File.separator+"return" + File.separator + " ")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(libraryService);
        }

        @Test
        void returnBookByInvalidISBN() throws Exception {
            doThrow(BookNotFoundException.class).when(libraryService).returnBook(TestDataUtils.ISBN);
            mockMvc.perform(put(BOOKS_URI+File.separator+"return" + File.separator +TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(libraryService).returnBook(TestDataUtils.ISBN);
        }

        @Test
        void returnBookByISBNWithNoReturnCopies() throws Exception {
            doThrow(ReturnCopiesNotAllowedException.class).when(libraryService).returnBook(TestDataUtils.ISBN);
            mockMvc.perform(put(BOOKS_URI+File.separator+"return" + File.separator +TestDataUtils.ISBN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotAcceptable());

            verify(libraryService).returnBook(TestDataUtils.ISBN);
        }

    }


}
