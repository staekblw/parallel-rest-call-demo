package com.toast.observable.demo;

import java.util.concurrent.ExecutionException;

import com.toast.observable.demo.api.service.BookService;
import com.toast.observable.demo.api.service.PenService;
import com.toast.observable.demo.api.service.ProductsIdServices;
import com.toast.observable.demo.service.ProductServices;
import com.toast.observable.demo.web.response.ProductsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

@RestController
public class WebDemo {
    @Autowired
    private PenService penService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ProductsIdServices productsIdServices;

    @GetMapping(value = "hello4")
    public String helloRawFuture() throws ExecutionException, InterruptedException {
        ProductServices productServices = new ProductServices(bookService, penService, productsIdServices);
        int i = 20;
        while (i > 0) {
            Observable<ProductsResponse> productsResponse = productServices.getProducts();
            productsResponse.subscribe(response -> {
                System.out.println(response);
            });
            i--;
        }
        return "yes";
    }

}
