package org.vsynytsyn.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerITest {

    @Autowired
    private BookRepository repository;


    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }


    @BeforeEach
    void initDatabase() {
        repository.save(new Book("9781408468418", "Ulysses", "James Joyce", LocalDateTime.now()));
        repository.save(new Book("9781408855652", "Harry Potter and the Philosopher's Stone", "Rowling J.K.",
                LocalDateTime.now()));
        repository.save(new Book("9780007538225", "Asylum", "Medellin Ry", LocalDateTime.now()));
    }


    @Test
    @Transactional
    void shouldReceiveBooksContainsGivenQuery() {
        String query = "81";
        Book[] books = RestAssured
                .when()
                .get(String.format("/api/books?q=%s", query))
                .then()
                .statusCode(200)
                .extract().body().as(Book[].class);

        Arrays.stream(books).forEach(book -> assertTrue(book.getIsbn().contains(query)));
    }


    @Test
    @Transactional
    void shouldSaveBook() {
        BookModel newBook = new BookModel("978-5-271-40351-4", "The withcer", "Andrzej Sapkowski");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/api/books/add")
                .then()
                .statusCode(200);

        Book[] books = RestAssured
                .when()
                .get("/api/books")
                .then()
                .statusCode(200)
                .extract().body().as(Book[].class);

        Optional<Book> optionalBook =
                Arrays.stream(books).filter(book -> book.getIsbn().equals(newBook.getIsbn())).findAny();
        assertTrue(optionalBook.isPresent());
        Book savedBook = optionalBook.get();
        assertEquals(newBook.getAuthor(), savedBook.getAuthor());
        assertEquals(newBook.getTitle(), savedBook.getTitle());
    }

}