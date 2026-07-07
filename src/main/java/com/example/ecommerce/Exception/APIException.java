package com.example.ecommerce.Exception;

public class APIException extends RuntimeException{
    public APIException(String message){
        super(message);
    }
}
