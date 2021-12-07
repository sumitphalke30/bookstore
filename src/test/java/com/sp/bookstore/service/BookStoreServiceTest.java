package com.sp.bookstore.service;

import com.sp.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class BookStoreServiceTest {

    @Autowired
    private BookStoreService bookStoreService;

    @BeforeEach
    public void setup() {
        bookStoreService.bookStore.clear();
    }

    @Test
    void performTransactionReceive() {
        String transaction = "receive,1617292605,50,24.79";

        bookStoreService.performTransaction(transaction);

        Book bookReceived = bookStoreService.bookStore.get(1617292605);

        assertEquals(bookReceived.getQuantity(), 50);
    }

    @Test
    void performTransactionBuy() {
        String transactionReceive = "receive,1617292605,50,24.79";

        String transactionBuy = "buy,1617292605,10";

        bookStoreService.performTransaction(transactionReceive);
        bookStoreService.performTransaction(transactionBuy);

        Book bookReceived = bookStoreService.bookStore.get(1617292605);

        assertEquals(bookReceived.getQuantity(), 40);
    }

    @Test
    void performTransactionMultiBuy() {
        String transactionReceive1 = "receive,1617292605,50,24.79";
        String transactionReceive2 = "receive,1449358624,25,21.75";

        String transactionBuy = "multibuy,1449358624,1,1617292605,10";

        bookStoreService.performTransaction(transactionReceive1);
        bookStoreService.performTransaction(transactionReceive2);
        bookStoreService.performTransaction(transactionBuy);

        Book bookReceived1 = bookStoreService.bookStore.get(1617292605);
        Book bookReceived2 = bookStoreService.bookStore.get(1449358624);

        assertEquals(bookReceived1.getQuantity(), 40);
        assertEquals(bookReceived2.getQuantity(), 24);
    }

    @Test
    void performTransactionReturn() {
        String transactionReceive = "receive,1617292605,50,24.79";

        String transactionReturn = "return,1617292605,5";

        bookStoreService.performTransaction(transactionReceive);
        bookStoreService.performTransaction(transactionReturn);

        Book bookReceived = bookStoreService.bookStore.get(1617292605);

        assertEquals(bookReceived.getQuantity(), 55);
    }

    @Test
    void performTransactionMultiReturn() {
        String transactionReceive1 = "receive,1617292605,50,24.79";
        String transactionReceive2 = "receive,1449358624,25,21.75";

        String transactionReturn = "multireturn,1617292605,3,1449358624,1";

        bookStoreService.performTransaction(transactionReceive1);
        bookStoreService.performTransaction(transactionReceive2);
        bookStoreService.performTransaction(transactionReturn);

        Book bookReceived1 = bookStoreService.bookStore.get(1617292605);
        Book bookReceived2 = bookStoreService.bookStore.get(1449358624);

        assertEquals(bookReceived1.getQuantity(), 53);
        assertEquals(bookReceived2.getQuantity(), 26);
    }
}