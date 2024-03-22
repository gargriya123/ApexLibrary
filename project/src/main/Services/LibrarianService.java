package com.library.project.Services;

import com.library.project.Model.Librarian;

public interface LibrarianService {
     Librarian addLibrarian(Librarian user);
    boolean checkEmail(String email);
    Librarian findLogin(String email,String password);
}
