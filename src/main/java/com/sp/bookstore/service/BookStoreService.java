package com.sp.bookstore.service;

import com.sp.bookstore.model.Book;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class BookStoreService {

    private final List<Book> bookstore = new ArrayList<>();

    public void initialize() {
        log.info("Initializing bookstore");

        bookstore.add(new Book(11, "BOOK1", 40));
        bookstore.add(new Book(15, "BOOK2", 23));
    }

    public void printBookStatus() {
        bookstore.forEach(
                bookInfo -> log.info("Book ID,Name: {}, {}", bookInfo.getId(), bookInfo.getBookName())
        );
    }
}
