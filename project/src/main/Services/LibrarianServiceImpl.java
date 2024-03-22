package com.library.project.Services;

import com.library.project.Model.Librarian;
import com.library.project.Repositories.LibrarianRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibrarianServiceImpl implements LibrarianService {


    public LibrarianServiceImpl() {
        super();
    }

    @Autowired
    private LibrarianRepo libRepo;

    @Override
    public Librarian addLibrarian(Librarian user) {

        return libRepo.save(user);
    }




    @Override
    public boolean checkEmail(String email) {

        return libRepo.existsByEmail(email);
    }
    @Override
    public Librarian findLogin(String email, String password) {
        return libRepo.findByEmailAndPassword(email,password);
    }

}
