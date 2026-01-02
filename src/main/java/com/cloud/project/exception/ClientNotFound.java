package com.cloud.project.exception;

public class ClientNotFound extends RuntimeException{
    public ClientNotFound(String message){
        super(message);
    }
}
