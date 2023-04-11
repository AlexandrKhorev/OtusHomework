package ru.otus.jdbc.exceptions;

public class EntityClassMetaDataException extends RuntimeException {
    public EntityClassMetaDataException(Exception ex) {
        super(ex);
    }

    public EntityClassMetaDataException(String msg) {
        super(msg);
    }
}
