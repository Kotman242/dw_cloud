package com.example.cloud.exception;

public class SaveFileException extends RuntimeException{

    public SaveFileException() {
        super();
    }

    public SaveFileException(String message) {
        super(message);
    }
}
