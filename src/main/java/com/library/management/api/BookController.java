package com.library.management.api;

import com.library.management.model.Book;
import com.library.management.service.LibraryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/books")
@RateLimiter(name = "library-management")
@Slf4j
public class BookController {

    private final LibraryService libraryService;

    /**
     * This api adds a new book into the library for a given valid book request.
     *
     * @param Book request.
     * @return Book along with HTTP status 201 (Created).
     * @throws 400 for bad request
     * @throws 409 for already existing Book with same ISBN
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        libraryService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * This api removes the existing book.
     *
     * @param isbn string path variable.
     * @throws 400 if isbn is null or blank
     * @throws 404 if book doesn't exist
     */
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> removeBook(@PathVariable String isbn) {
        if (StringUtils.isBlank(isbn)) {
            return ResponseEntity.badRequest().build();
        }
        libraryService.removeBook(isbn);
        return ResponseEntity.ok().build();
    }

    /**
     * This api gets the existing book for the ISBN provided.
     *
     * @param isbn string path variable.
     * @return Book
     * @throws 400 if isbn is null or blank
     * @throws 404 if book doesn't exist
     */
    @GetMapping(value = "/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> findBookById(@PathVariable String isbn) {
        if (StringUtils.isBlank(isbn)) {
            return ResponseEntity.badRequest().build();
        }
        Book book = libraryService.findBookByISBN(isbn);
        return ResponseEntity.ok(book);
    }

    /**
     * This api gets all existing books for the author provided.
     *
     * @param author string path variable.
     * @return list of Books
     * @throws 400 if author is null or blank
     */
    @GetMapping(value="/author/{author}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> findBooksByAuthor(@PathVariable String author) {
        if (StringUtils.isBlank(author)) {
            return ResponseEntity.badRequest().build();
        }
        List<Book> books = libraryService.findBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    /**
     * This api decrements the available copies by 1 for the isbn associated.
     *
     * @param isbn string path variable.
     * @return Book
     * @throws 400 if isbn is null or blank
     * @throws 404 if book doesn't exist
     * @throws 409 if no more available copies to borrow
     */
    @PutMapping(value="/borrow/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> borrowBook(@PathVariable String isbn) {
        if (StringUtils.isBlank(isbn)) {
            return ResponseEntity.badRequest().build();
        }
        Book book = libraryService.borrowBook(isbn);
        return ResponseEntity.ok(book);
    }

    /**
     * This api increment the available copies by 1 for the isbn associated.
     *
     * @param isbn string path variable.
     * @return Book
     * @throws 400 if isbn is null or blank
     * @throws 404 if book doesn't exist
     * @throws 409 if mismatch to initial copies and available copies
     */
    @PutMapping(value="/return/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> returnBook(@PathVariable String isbn) {
        if (StringUtils.isBlank(isbn)) {
            return ResponseEntity.badRequest().build();
        }
        Book book = libraryService.returnBook(isbn);
        return ResponseEntity.ok(book);
    }


}
