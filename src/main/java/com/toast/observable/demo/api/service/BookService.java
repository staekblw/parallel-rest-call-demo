package com.toast.observable.demo.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.toast.observable.demo.api.model.Book;
import com.toast.observable.demo.api.model.Pen;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;
@Component
public class BookService {
    private AsyncRestTemplate template = new AsyncRestTemplate();

    @HystrixCommand(fallbackMethod = "fallBack")
    public Observable<Book> by(String id) {
        System.out.println("book nomarll");
        return Observable.create(x -> {
            ListenableFuture<ResponseEntity<String>> forEntity = template.getForEntity("11http://www.baidu.com", String.class);
            forEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    x.onError(ex);
                }

                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    x.onNext(new Book(id));
                    x.onCompleted();
                }
            });
        });

    }

    public Book fallBack(String id) {
        System.out.println("book fallback");
        Book failBook = new Book("FailBook");
        return failBook;
    }
}