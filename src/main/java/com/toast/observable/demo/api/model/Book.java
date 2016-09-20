package com.toast.observable.demo.api.model;

/**
 * Created by lbwang on 9/20/16.
 */
public class Book {
    private String id;

    public Book(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                '}';
    }
}
