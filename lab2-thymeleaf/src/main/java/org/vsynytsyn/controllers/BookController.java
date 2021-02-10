package org.vsynytsyn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.service.BookService;

import java.util.List;

@Controller
@RequestMapping({"/", "/books"})
public class BookController {

    private final BookService service;


    public BookController(BookService service) {
        this.service = service;
    }


    @GetMapping
    String getAll(Model model) {
        model.addAttribute("name", "name");
        List<Book> books = service.getAll();
        model.addAttribute("books", books);

        return "books";
    }


    @GetMapping("/add")
    String addForm() {
        return "books_form";
    }


    @PostMapping("/add")
    String addNew(@ModelAttribute("book") BookModel bookModel, Model model) {
        if (bookModel.getIsbn().isBlank() || bookModel.getTitle().isBlank() || bookModel.getAuthor().isBlank()) {
            model.addAttribute("invalidInput", true);
            return "books_form";
        }
        service.saveBook(bookModel);
        return "redirect:/books";
    }
}
