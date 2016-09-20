package com.toast.observable.demo.api.model;

/**
 * Created by lbwang on 9/20/16.
 */
public class Pen {
    private String id;

    public Pen(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pen{" +
                "id='" + id + '\'' +
                '}';
    }
}
