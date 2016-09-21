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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

@Component
public class ProductServices {
    private Predicate<String> isBook = id -> id.startsWith("book");
    private Predicate<String> isPen = id -> id.startsWith("pen");
    private final BookService bookService;
    private final PenService penService;
    private final ProductsIdServices productsIdServices;


    @Autowired
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
                        return new ProductsResponse(books, pens.stream().filter(x -> x != null).collect(Collectors.toList()));
                    });
        }).flatMap(products -> products);
    }

    private Observable<List<Pen>> pens(List<String> ids) {
        Stream<Observable<Pen>> pens = ids.stream().filter(isPen)//
                .map(id -> {
                    return penService.by(id).onErrorReturn(ex -> null);
                });
        return toOneObservable(pens);// a stream of Observable to one Observable hold a list
    }

    private <T> Observable<List<T>> toOneObservable(Stream<Observable<T>> observables) {
        return Observable.merge(observables.collect(Collectors.toList())).toList();
    }

    private Observable<List<Book>> books(List<String> ids) {
        Stream<Observable<Book>> books = ids.stream().filter(isBook)//
                .map(id -> {
                    return bookService.by(id);
                });
        return toOneObservable(books);
    }
}
