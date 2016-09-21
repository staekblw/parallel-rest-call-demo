package com.toast.observable.demo;

import com.toast.observable.demo.api.service.ProductsIdServices;
import com.toast.observable.demo.api.service.BookService;
import com.toast.observable.demo.api.service.PenService;
import com.toast.observable.demo.web.response.ProductsResponse;
import com.toast.observable.demo.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import rx.Observable;

@EnableHystrix
@EnableAutoConfiguration
@Component
public class ApplicationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext("com.toast.*");
        ProductServices productServices = configApplicationContext.getBean(ProductServices.class);
        int i = 20;
        while (i > 0) {
            System.out.println("call started");
            Observable<ProductsResponse> productsResponse = productServices.getProducts();
            productsResponse.subscribe(response -> {
                System.out.println(response);
            });
            i--;
        }
    }
}