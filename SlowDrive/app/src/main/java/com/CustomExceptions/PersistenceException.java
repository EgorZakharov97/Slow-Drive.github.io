package com.CustomExceptions;

public class PersistenceException extends RuntimeException {
    public PersistenceException(final Exception cause) {
        super(cause);
    }
}

