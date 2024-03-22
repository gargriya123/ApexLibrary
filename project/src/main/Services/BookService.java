package com.library.project.Services;

import com.library.project.Model.Book;

import java.util.List;

public interface BookService {
    boolean bookExist(String isbn);
    Book saveBook(Book book);
    List<Book> getAllBooks();
}
