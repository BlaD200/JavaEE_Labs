package org.vsynytsyn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vsynytsyn.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
