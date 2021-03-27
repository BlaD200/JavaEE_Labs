package org.vsynytsyn.repository.impl;

import org.vsynytsyn.domain.Book;
import org.vsynytsyn.repository.BookRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Book> findByIsbnOrTitle(String isbn, String title) {
        // "select b from Book b where b.isbn like (:isbn)";

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> root = cr.from(Book.class);

        LinkedList<Predicate> predicates = new LinkedList<>();
        if (!isbn.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("isbn")), "%%%s%%".formatted(isbn).toLowerCase()));
        }
        if (!title.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%%%s%%".formatted(title).toLowerCase()));
        }

        Predicate predicate = cb.and(predicates.toArray(Predicate[]::new));
        cr.where(predicate);

        TypedQuery<Book> query = entityManager.createQuery(cr);
        return query.getResultList();
    }
}
