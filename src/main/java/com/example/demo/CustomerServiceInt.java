package com.example.demo;

public interface CustomerServiceInt {

    boolean submitRequest(RequestMessage requestMessage);

    boolean deleteRequest(DeleteMessage deleteMessage);

}
