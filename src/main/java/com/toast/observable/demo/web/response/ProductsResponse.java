package com.toast.observable.demo.web.response;

import java.util.List;

import com.toast.observable.demo.api.model.Book;
import com.toast.observable.demo.api.model.Pen;

/**
 * Created by lbwang on 9/20/16.
 */
public class ProductsResponse {
    private final List<Book> books;
    private final List<Pen> pens;

    @Override
    public String toString() {
        return "ProductsResponse{" +
                "books=" + books +
                ", pens=" + pens +
                '}';
    }

    public ProductsResponse(List<Book> books, List<Pen> pens) {
        this.books = books;
        this.pens = pens;
    }
}
