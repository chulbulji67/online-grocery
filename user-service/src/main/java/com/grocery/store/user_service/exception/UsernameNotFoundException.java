package com.grocery.store.user_service.exception;

public class UsernameNotFoundException extends RuntimeException{
    String message;
    UsernameNotFoundException(String  message){
        this.message = message;
    }
}
