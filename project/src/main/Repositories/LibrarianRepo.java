package com.library.project.Repositories;

import com.library.project.Model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibrarianRepo extends JpaRepository<Librarian,Integer> {
     boolean existsByEmail(String email);
     Librarian findByEmailAndPassword(String email,String password);

}
