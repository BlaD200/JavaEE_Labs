package org.vsynytsyn.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.service.BookService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    private final static List<Book> bookList = List.of(
            new Book("9781408468418", "Ulysses", "James Joyce", LocalDateTime.now()),
            new Book("9781408855652", "Harry Potter and the Philosopher's Stone", "Rowling J.K.", LocalDateTime.now()),
            new Book("9780007538225", "Asylum", "Medellin Ry", LocalDateTime.now())
    );
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookService serviceMock;


    private static Stream<Arguments> provideQueriesForMatchesQuery() {
        return Stream.of(
                Arguments.of("9781408468418", List.of(bookList.get(0))),
                Arguments.of("AND", List.of(bookList.get(1))),
                Arguments.of("81",
                        bookList.stream().filter(book -> book.getIsbn().contains("81")).collect(Collectors.toList()))
        );
    }


    @Test
    void shouldReceiveAllBooks() throws Exception {
        String expectedResult = mapper.writeValueAsString(bookList);
        when(serviceMock.getAll()).thenReturn(bookList);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/books")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

        verify(serviceMock, times(1)).getAll();
        verifyNoMoreInteractions(serviceMock);
    }


    @ParameterizedTest
    @MethodSource("provideQueriesForMatchesQuery")
    void shouldReceiveAllBooksMatchesQuery(String query, List<Book> expectedBooks) throws Exception {
        String expectedResult = mapper.writeValueAsString(expectedBooks);
        when(serviceMock.getByNameOrIsbn(query)).thenReturn(expectedBooks);


        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/books?q=%s", query))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

        verify(serviceMock, times(1)).getByNameOrIsbn(query);
        verifyNoMoreInteractions(serviceMock);
    }


    @ParameterizedTest
    @CsvSource(value = {
            " ,Asylum,Medellin Ry",
            "9780007538225, ,Medellin Ry",
            "9780007538225,Asylum, "
    }, emptyValue = " ")
    void shouldReturn_UnprocessableEntityStatus_WithEmptyFieldsWhenAdd(String isbn, String title, String author)
            throws Exception {
        String newBook = mapper.writeValueAsString(new BookModel(isbn, title, author));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBook)
        ).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        verifyNoInteractions(serviceMock);
    }


    @Test
    void shouldReturn_ConflictStatus_WhenAddingBookWithExistingISBN() throws Exception {
        when(serviceMock.saveBook(ArgumentMatchers.any())).thenReturn(false);
        String newBook = mapper.writeValueAsString(bookList.get(0));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBook)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }
}