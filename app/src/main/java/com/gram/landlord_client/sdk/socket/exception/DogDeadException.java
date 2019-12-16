package com.gram.landlord_client.sdk.socket.exception;

public class DogDeadException extends RuntimeException {
    public DogDeadException() {
        super();
    }

    public DogDeadException(String message) {
        super(message);
    }

    public DogDeadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DogDeadException(Throwable cause) {
        super(cause);
    }

}
