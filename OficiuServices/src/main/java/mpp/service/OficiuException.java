package mpp.service;

public class OficiuException extends Exception{
    public OficiuException() {
        super();
    }

    public OficiuException(String message) {
        super(message);
    }

    public OficiuException(String message, Throwable cause) {
        super(message, cause);
    }
}
