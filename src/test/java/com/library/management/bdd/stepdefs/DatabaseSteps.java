package com.library.management.bdd.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.model.BookBean;
import com.library.management.repository.BookRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.library.management.util.FileReader.fromFile;
import static org.junit.jupiter.api.Assertions.*;

@ScenarioScope
public class DatabaseSteps {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private BookRepository bookRepository;

    @Given("the database table BOOKS is updated with data {string}")
    public void saveBook(final String filename) throws JsonProcessingException {
        BookBean bookBean = OBJECT_MAPPER.readValue(fromFile(filename), BookBean.class);
        bookRepository.save(bookBean);
    }

    @And("the database table BOOKS doesnt contain isbn {string}")
    public void isbnNotExists(final String isbn) {
        Optional<BookBean> bookBean = bookRepository.findById(isbn);
        assertFalse(bookBean.isPresent());
    }

    @And("the database table BOOKS contains isbn {string}")
    public void isbnExists(final String isbn) {
        Optional<BookBean> bookBean = bookRepository.findById(isbn);
        assertTrue(bookBean.isPresent());
    }

    @And("the database table BOOKS contains isbn {string} with available copies {int}")
    public void verifyAvailableCopies(final String isbn, final int availableCount) {
        Optional<BookBean> bookBean = bookRepository.findById(isbn);
        assertTrue(bookBean.isPresent());
        assertEquals(availableCount, bookBean.get().getAvailableCopies());
    }

    @And("the database table BOOKS contains author {string}")
    public void authorExists(final String author) {
        List<BookBean> byAuthor = bookRepository.findByAuthor(author);
        assertFalse(byAuthor.isEmpty());
    }

    @And("the database table BOOKS contains the attributes defined in {string} for isbn {string}")
    public void tableBooksContainsAttributesDefinedInFile(final String filename, final String isbn ) throws JsonProcessingException {
        BookBean expected = OBJECT_MAPPER.readValue(fromFile(filename), BookBean.class);
        Optional<BookBean> actual = bookRepository.findById(isbn);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }


}
