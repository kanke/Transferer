package org.revolut.exception;

import javax.ws.rs.WebApplicationException;

public class AccountException extends WebApplicationException {
    public AccountException(String message) {
        super(message);
    }
}
