package org.vsynytsyn.repository;

import org.vsynytsyn.domain.Book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByIsbnOrTitle(String isbn, String title);
}
