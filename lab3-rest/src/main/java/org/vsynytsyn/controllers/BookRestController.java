package org.vsynytsyn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService service;


    public BookRestController(BookService service) {
        this.service = service;
    }


    @GetMapping()
    public List<Book> getByTitleOrIsbn(@RequestParam(value = "q", required = false) String q) {
        if (q != null)
            return service.getByNameOrIsbn(q);
        else
            return service.getAll();
    }


    @PostMapping("/add")
    public ResponseEntity<Object> addNew(@RequestBody BookModel bookModel, Model model) {
        if (bookModel.getIsbn().isBlank() || bookModel.getTitle().isBlank() || bookModel.getAuthor().isBlank()) {
            model.addAttribute("invalidInput", true);
        }
        if (service.saveBook(bookModel))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
