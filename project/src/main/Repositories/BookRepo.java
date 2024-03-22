package com.library.project.Repositories;

import com.library.project.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Integer> {
   boolean existsByIsbn(String isbn);
}
