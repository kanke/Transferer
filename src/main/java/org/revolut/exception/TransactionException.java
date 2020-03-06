package org.revolut.exception;

import javax.ws.rs.WebApplicationException;

public class TransactionException extends WebApplicationException {
    public TransactionException(String message) {
        super(message);
    }
}
