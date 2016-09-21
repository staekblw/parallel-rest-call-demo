package com.toast.observable.demo.api.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;
import rx.Observable;

@Component
public class ProductsIdServices {

    private AsyncRestTemplate template = new AsyncRestTemplate();

    public Observable<List<String>> getProductIds() {
        return new GetProductIdsCommand("product").toObservable();
    }

    class GetProductIdsCommand extends HystrixObservableCommand {
        protected GetProductIdsCommand(String key) {
            super(HystrixCommandGroupKey.Factory.asKey(key));
        }

        @Override
        protected Observable construct() {
            System.out.println("**********LINBO");
            return Observable.create(x -> {
                ListenableFuture<ResponseEntity<String>> listenableFuture = template.getForEntity("1http://www.baidu.com1", String.class);
                listenableFuture.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        x.onError(ex);
                    }

                    @Override
                    public void onSuccess(ResponseEntity<String> result) {
                        x.onNext(Arrays.asList("pen1", "book2", "book3"));
                        x.onCompleted();
                    }
                });
            });
        }

        @Override
        protected Observable resumeWithFallback() {
            return Observable.just(Arrays.asList("bookfail", "penfail"));
        }
    }

}