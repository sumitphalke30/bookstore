package com.sp.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    private int id;
    private String bookName;
    private int quantity;
    private BigDecimal price;
}
