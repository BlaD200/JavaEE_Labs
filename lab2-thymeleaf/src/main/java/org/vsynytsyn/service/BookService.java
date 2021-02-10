package org.vsynytsyn.service;

import org.springframework.stereotype.Service;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;


    public BookService(BookRepository repository) {
        this.repository = repository;
    }


    public List<Book> getAll() {
        return repository.findAll();
    }


    public Optional<Book> getOne(String isbn) {
        return repository.findById(isbn);
    }


    public void saveBook(BookModel bookModel) {
        repository.save(
                new Book(bookModel.getIsbn(), bookModel.getTitle(), bookModel.getAuthor(),
                        LocalDateTime.now()
                ));
    }
}
