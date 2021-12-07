package com.sp.bookstore.service;

import com.sp.bookstore.model.Book;
import com.sp.bookstore.model.BookTransactionAction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Component
@Log4j2
public class BookStoreService {

    @Autowired
    private  TransactionParser transactionParser;

    public HashMap<Integer, Book> bookStore = new HashMap<>();

    public void printBookStatus() {
      log.info("Inventory:");
        bookStore.forEach(
                (bookId, bookInfo) -> log.info("{}, {}, {}", bookInfo.getId(), bookInfo.getQuantity(), bookInfo.getPrice())
        );
    }

    public void performTransaction(String transaction) {
        if(!transaction.isEmpty()) {
            String action = transaction.split(",")[0];
            //log.info("Action: {}", action);

            List<Book> bookRequestedList = transactionParser.getBooks(action, transaction);

            switch (action) {
                case BookTransactionAction.BUY:
                case BookTransactionAction.MULTI_BUY:
                    bookRequestedList.forEach(this::buy); break;
                case BookTransactionAction.RECEIVE: bookRequestedList.forEach(this::receive); break;
                case BookTransactionAction.QUERY: bookRequestedList.forEach(this::query); break;
                case BookTransactionAction.UPDATE_PRICE: bookRequestedList.forEach(this::updatePrice); break;
                case BookTransactionAction.RETURN:
                case BookTransactionAction.MULTI_RETURN:
                    bookRequestedList.forEach(this::returnBook); break;
            }
        }
    }

    private void returnBook(Book bookRequested) {
        if(bookStore.containsKey(bookRequested.getId())) {
            Book bookStoreRecord = bookStore.get(bookRequested.getId());
            bookStoreRecord.setQuantity(bookStoreRecord.getQuantity() + bookRequested.getQuantity());
            bookStore.put(bookStoreRecord.getId(), bookStoreRecord);
            BigDecimal total = bookStoreRecord.getPrice().multiply(new BigDecimal(bookRequested.getQuantity())).negate();
            log.info("Refund {} copies of {} at {} each. Total: {}", bookRequested.getQuantity(), bookRequested.getId(), bookStoreRecord.getPrice(), total);
        } else {
            log.error("Book {} not available!", bookRequested.getId());
        }
    }

    private void updatePrice(Book bookRequested) {
        if(bookStore.containsKey(bookRequested.getId())) {
            Book bookStoreRecord = bookStore.get(bookRequested.getId());
            bookStoreRecord.setPrice(bookRequested.getPrice());
            bookStore.put(bookStoreRecord.getId(), bookStoreRecord);
            log.info("New price for {}: {} each", bookRequested.getId(), bookRequested.getPrice());
        } else {
            log.error("Book {} not available!", bookRequested.getId());
        }

    }

    private void query(Book bookRequested) {
        if(bookStore.containsKey(bookRequested.getId())) {
            Book bookStoreRecord = bookStore.get(bookRequested.getId());
            log.info("There are {} copies of {}. Price:{} each.", bookStoreRecord.getQuantity(), bookStoreRecord.getId(), bookStoreRecord.getPrice());

        } else {
            log.error("Book {} not available!", bookRequested.getId());
        }

    }

    private void receive(Book bookRequested) {
        if(bookStore.containsKey(bookRequested.getId())) {
            Book bookStoreRecord = bookStore.get(bookRequested.getId());
            bookStoreRecord.setQuantity(bookRequested.getQuantity() + bookStoreRecord.getQuantity());
            bookStore.put(bookRequested.getId(), bookStoreRecord);
        } else {
            bookStore.put(bookRequested.getId(), bookRequested);
        }
        log.info("Received {} copies of book {}", bookRequested.getQuantity(), bookRequested.getId());
    }

    private void buy(Book bookRequested) {
        if(bookStore.containsKey(bookRequested.getId())) {
            Book bookStoreRecord = bookStore.get(bookRequested.getId());

            if(bookStoreRecord.getQuantity() >= bookRequested.getQuantity()) {
                bookStoreRecord.setQuantity(bookStoreRecord.getQuantity() - bookRequested.getQuantity());
                bookStore.put(bookRequested.getId(), bookStoreRecord);

                BigDecimal total = bookStoreRecord.getPrice().multiply(new BigDecimal(bookRequested.getQuantity()));
                log.info("Sold {} copies of book {} at {} each. Total: {}", bookRequested.getQuantity(), bookStoreRecord.getId(), bookStoreRecord.getPrice(), total);
            } else {
                log.error("Insufficient quantity of {}", bookRequested.getId());
            }
        } else {
            log.error("Book {} not available!", bookRequested.getId());
        }
    }
}
