package com.library.project.Repositories;

import com.library.project.Model.IssueBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepo extends JpaRepository<IssueBook,Integer> {
    boolean existsByUserIdAndBookId(int userId,int bookId);
    IssueBook findByUserIdAndBookId(int userId,int bookId);
}
