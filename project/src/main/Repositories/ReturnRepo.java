package com.library.project.Repositories;

import com.library.project.Model.ReturnBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRepo extends JpaRepository<ReturnBook,Integer> {
}
