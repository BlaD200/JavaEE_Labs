package org.vsynytsyn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.service.BookService;

import java.util.List;

@Controller
@RequestMapping({"/"})
public class BookController {

    private final BookService service;


    public BookController(BookService service) {
        this.service = service;
    }


    @GetMapping
    String getAll(Model model) {
        List<Book> books = service.getAll();
        model.addAttribute("books", books);

        return "index";
    }
}
