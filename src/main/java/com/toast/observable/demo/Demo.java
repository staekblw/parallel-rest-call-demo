package com.toast.observable.demo;

import com.toast.observable.demo.api.service.ProductsIdServices;
import com.toast.observable.demo.api.service.BookService;
import com.toast.observable.demo.api.service.PenService;
import com.toast.observable.demo.web.response.ProductsResponse;
import com.toast.observable.demo.service.ProductServices;
import rx.Observable;

public class Demo {
    public static void main(String[] args) {
        ProductServices productServices = new ProductServices(new BookService(), new PenService(), new ProductsIdServices());
        Observable<ProductsResponse> productsResponse = productServices.getProducts();
        productsResponse.subscribe(response -> {
            System.out.println(response);
        });
    }
}