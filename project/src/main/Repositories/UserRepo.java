package com.library.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.library.project.Model.User;
public interface UserRepo extends JpaRepository<User,Integer> {
  boolean existsByEmail(String email);
}
