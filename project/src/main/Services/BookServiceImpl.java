package com.library.project.Services;

import com.library.project.Model.Book;
import com.library.project.Repositories.BookRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private BookRepo bookRepo;


    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public boolean bookExist(String isbn) {
        return bookRepo.existsByIsbn(isbn);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepo.save(book);
    }



}
