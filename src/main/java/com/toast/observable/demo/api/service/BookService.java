package com.toast.observable.demo.api.service;

import com.toast.observable.demo.api.model.Book;
import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;

public class BookService {
    private AsyncRestTemplate template = new AsyncRestTemplate();

    public Observable<Book> by(String id) {
        return Observable.from(template.getForEntity("http://www.baidu.com", String.class))//
                .map(x -> new Book(id));
    }
}