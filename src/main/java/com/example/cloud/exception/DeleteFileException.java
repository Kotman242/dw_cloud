package com.example.cloud.exception;

public class DeleteFileException extends RuntimeException{

    public DeleteFileException() {
        super();
    }

    public DeleteFileException(String message) {
        super(message);
    }
}
