package com.sp.bookstore.service;

import com.sp.bookstore.model.Book;
import com.sp.bookstore.model.BookTransactionAction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionParser {
    public List<Book> getBooks(String action, String transaction) {
        List<Book> list = new ArrayList<>();

        String[] transactionArgs = transaction.split(",");

        if (action.contains("multi")) {
            Book book = new Book();
            book.setId(Integer.parseInt(transactionArgs[1]));
            book.setQuantity(Integer.parseInt(transactionArgs[2]));

            list.add(book);

            book = new Book();
            book.setId(Integer.parseInt(transactionArgs[3]));
            book.setQuantity(Integer.parseInt(transactionArgs[4]));

            list.add(book);

        } else if (action.equalsIgnoreCase(BookTransactionAction.UPDATE_PRICE)) {
            Book book = new Book();
            book.setId(Integer.parseInt(transactionArgs[1]));
            book.setPrice(new BigDecimal(transactionArgs[2]));

            list.add(book);
        } else if (action.equalsIgnoreCase(BookTransactionAction.RETURN)) {
            Book book = new Book();
            book.setId(Integer.parseInt(transactionArgs[1]));
            book.setQuantity(Integer.parseInt(transactionArgs[2]));

            list.add(book);
        } else {
            Book book = new Book();
            book.setId(Integer.parseInt(transactionArgs[1]));

            if (transactionArgs.length > 2) {
                book.setQuantity(Integer.parseInt(transactionArgs[2]));
            }

            if(transactionArgs.length > 3) {
                book.setPrice(new BigDecimal(transactionArgs[3]));
            }
            list.add(book);
        }




        return list;
    }
}
