package com.toast.observable.demo.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.toast.observable.demo.api.model.Pen;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;

@Component
public class PenService {

    private AsyncRestTemplate template = new AsyncRestTemplate();

    @HystrixCommand(fallbackMethod = "fallBack")
    public Observable<Pen> by(String id) {
        System.out.println("pen nomarll");
        return Observable.create(x -> {
            template.getForEntity("11http://www.baidu.com", String.class).addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    x.onError(ex);
                }

                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    x.onNext(new Pen(id));
                    x.onCompleted();
                }
            });
        });

    }

    public Pen fallBack(String id) {
        System.out.println("pen fallback");
        return new Pen("default pen");
    }
}