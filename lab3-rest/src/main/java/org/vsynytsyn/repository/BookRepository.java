package org.vsynytsyn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vsynytsyn.domain.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findAllByIsbnContainingOrTitleContainingIgnoreCase(String isbn, String title);
}
