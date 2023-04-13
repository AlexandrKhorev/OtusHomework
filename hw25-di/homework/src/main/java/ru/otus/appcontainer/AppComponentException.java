package ru.otus.appcontainer;

public class AppComponentException extends RuntimeException {
    public AppComponentException(Exception ex) {
        super(ex);
    }

    public AppComponentException(String msg) {
        super(msg);
    }
}
