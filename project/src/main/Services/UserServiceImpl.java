package com.library.project.Services;

import com.library.project.Model.User;
import com.library.project.Repositories.BookRepo;
import com.library.project.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

@Autowired
private BookRepo bookRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }





}
