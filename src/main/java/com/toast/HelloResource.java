package com.toast;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
/*
both hello1 hello2 hello3 did the some thing with different ways.
first it make a call to check the server status(only check http stats code for demo),
then make two more call and aggregate the result together(only the length of each payload for demo)
hello1 use raw java future, which is blocking
hello2 use CompletableFuture which no longer blocking, however the aggregate payload is kind of messy
hello3 use RX Observable, which is very easy for aggregate the payload
 */
@RestController
public class HelloResource {

    private static final String TASK_FAILED = "task failed";
    private AsyncRestTemplate template = new AsyncRestTemplate();
    private String url1 = "http://www.baidu.com";
    private String url2 = "http://www.baidu.com";
    private String url3 = "http://www.baidu.com";

    @GetMapping(value = "hello1")
    public String helloRawFuture() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<String>> payloadFuture1 = template.getForEntity(url1, String.class);
        ListenableFuture<ResponseEntity<String>> payloadFuture2 = template.getForEntity(url2, String.class);
        ListenableFuture<ResponseEntity<String>> payloadFuture3 = template.getForEntity(url3, String.class);

        if (remoteServerAvailable(payloadFuture1.get())) {
            //Blocking the current thread
            return aggregatePayload(payloadFuture2.get(), payloadFuture3.get());
        } else {
            return TASK_FAILED;
        }
    }

    @GetMapping(value = "hello2")
    public DeferredResult helloCompletableFuture() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<String>> payloadFuture1 = template.getForEntity(url1, String.class);
        CompletableFuture<?> completableFuture =
                CompletableFuturesUtil.toCompletableFuture(payloadFuture1).thenCompose(x -> {
                    if (remoteServerAvailable(x)) {
                        ListenableFuture<ResponseEntity<String>> payloadFuture2 = template.getForEntity(url2, String.class);
                        ListenableFuture<ResponseEntity<String>> payloadFuture3 = template.getForEntity(url3, String.class);
                        try {
                            return CompletableFuturesUtil.toCompletableFuture(payloadFuture2)
                                    .thenCombine(CompletableFuturesUtil.toCompletableFuture(payloadFuture3), this::aggregatePayload);
                            //aggregate the payloads are difficult, here only aggregate two payload. Imagine 10 payloads
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        return CompletableFuture.completedFuture(TASK_FAILED);
                    }
                });

        DeferredResult deferredResult = new DeferredResult();
        completableFuture.whenComplete((result, ex) -> {
            deferredResult.setResult(result);
        });
        return deferredResult;
    }

    @GetMapping(value = "hello3")
    public DeferredResult helloRXObservable() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<String>> payloadFuture1 = template.getForEntity(url1, String.class);

        Observable<Object> resultOb = Observable.from(payloadFuture1).flatMap(x -> {
            ListenableFuture<ResponseEntity<String>> payloadFuture2 = template.getForEntity(url2, String.class);
            ListenableFuture<ResponseEntity<String>> payloadFuture3 = template.getForEntity(url3, String.class);
            if (remoteServerAvailable(x)) {
                Observable<String> observable = Observable.zip(Observable.from(payloadFuture2), Observable.from(payloadFuture3), (payloadA, payloadB) -> {
                    return aggregatePayload(payloadA, payloadB);
                });
                return observable;
            }
            return Observable.just(TASK_FAILED);
        });
        DeferredResult deferredResult = new DeferredResult();
        resultOb.subscribe((result) -> {
            deferredResult.setResult(result);
        });
        return deferredResult;
    }

    private boolean remoteServerAvailable(ResponseEntity<String> x) {
        return x.getStatusCode() == HttpStatus.OK;
    }

    private <V extends String, U> String aggregatePayload(ResponseEntity<V> payloadA, ResponseEntity<V> payloadB) {
        return String.format("%d:%d", payloadA.getBody().length(), payloadB.getBody().length());
    }
}
