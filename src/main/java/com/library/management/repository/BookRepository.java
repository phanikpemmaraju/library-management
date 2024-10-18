package com.library.management.repository;

import com.library.management.model.BookBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookBean, String> {
    List<BookBean> findByAuthor(String author);
}

