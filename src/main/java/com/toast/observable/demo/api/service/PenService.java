package com.toast.observable.demo.api.service;

import com.toast.observable.demo.api.model.Pen;
import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;

public class PenService {

    private AsyncRestTemplate template = new AsyncRestTemplate();

    public Observable<Pen> by(String id) {
        return Observable.from(template.getForEntity("http://www.baidu.com", String.class))//
                .map(x -> new Pen(id));
    }
}