package org.vsynytsyn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.service.BookService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    public ResponseEntity<Object> addNew(@RequestBody BookModel bookModel) {
        for (Method method : bookModel.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
                try {
                    String attr = (String) method.invoke(bookModel);
                    if (attr == null || attr.isBlank())
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                } catch (IllegalAccessException | InvocationTargetException ignore) {
                }
            }
        }
        if (service.saveBook(bookModel))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
