package org.vsynytsyn.service;

import org.springframework.stereotype.Service;
import org.vsynytsyn.domain.Book;
import org.vsynytsyn.dto.BookModel;
import org.vsynytsyn.repository.BookRepository;

import javax.validation.ConstraintViolationException;
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


    public boolean saveBook(BookModel bookModel) {
        try {
            if (repository.findById(bookModel.getIsbn()).isPresent())
                return false;
            repository.save(
                    new Book(bookModel.getIsbn().strip(), bookModel.getTitle().strip(), bookModel.getAuthor().strip(),
                            LocalDateTime.now()
                    )
            );
            return true;
        } catch (ConstraintViolationException e) {
            return false;
        }
    }


    public List<Book> getByNameOrIsbn(String searchText) {
        return repository.findAllByIsbnContainingOrTitleContainingIgnoreCase(searchText, searchText);
    }
}
