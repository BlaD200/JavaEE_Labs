package org.vsynytsyn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.service.BookService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping({"/"})
public class BookController {

    private final BookService service;


    public BookController(BookService service) {
        this.service = service;
    }


    @GetMapping()
    public String getByTitleOrIsbn(@RequestParam(value = "q", required = false) String q, Model model) {
        List<Book> books;
        if (q != null)
            books = service.getByNameOrIsbn(q);
        else
            books = service.getAll();
        model.addAttribute("books", books);
        return "index";
    }


    @GetMapping("/book/{isbn}")
    String getOne(@PathVariable("isbn") String isbn, Model model) {
        Optional<Book> optionalBook = service.getOne(isbn);
        model.addAttribute("book", optionalBook);
        return "book";
    }


    @GetMapping("/admin")
    String addForm() {
        return "admin";
    }
}
