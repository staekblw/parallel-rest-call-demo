package com.toast.observable.demo.api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;

public class ProductsIdServices {

    private AsyncRestTemplate template = new AsyncRestTemplate();

    public Observable<List<String>> getProductIds() {
        return Observable.from(template.getForEntity("http://www.baidu.com", String.class))//
                .map(s -> Arrays.asList("pen1", "book2", "book3"));
    }
}