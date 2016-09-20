package com.toast.observable.demo.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.toast.observable.demo.api.model.Book;
import com.toast.observable.demo.api.model.Pen;
import com.toast.observable.demo.api.service.BookService;
import com.toast.observable.demo.api.service.PenService;
import com.toast.observable.demo.api.service.ProductsIdServices;
import com.toast.observable.demo.web.response.ProductsResponse;
import rx.Observable;

public class ProductServices {
    private Predicate<String> isBook = id -> id.startsWith("book");
    private Predicate<String> isPen = id -> id.startsWith("pen");
    private final BookService bookService;
    private final PenService penService;
    private final ProductsIdServices productsIdServices;

    public ProductServices(BookService bookService, PenService penService, ProductsIdServices productsIdServices) {
        this.bookService = bookService;
        this.penService = penService;
        this.productsIdServices = productsIdServices;
    }

    public Observable<ProductsResponse> getProducts() {
        return productsIdServices.getProductIds().map(id -> {
            return Observable.zip(//
                    books(id),//
                    pens(id),//
                    (books, pens) -> {
                        return new ProductsResponse(books, pens);
                    });
        }).flatMap(products -> products);
    }

    private Observable<List<Pen>> pens(List<String> ids) {
        Stream<Observable<Pen>> pens = ids.stream().filter(isPen)//
                .map(id -> {
                    return penService.by(id);
                });
        return Observable.merge(pens.collect(Collectors.toList())).toList();
    }

    private Observable<List<Book>> books(List<String> ids) {
        Stream<Observable<Book>> giPolicies = ids.stream().filter(isBook)//
                .map(id -> {
                    return bookService.by(id);
                });
        return Observable.merge(giPolicies.collect(Collectors.toList())).toList();
    }
}
