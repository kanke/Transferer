package org.revolut.service;

import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.AccountTransactionException;

public interface AccountTransactionService {
    void transferFunds(AccountTransactionDto accountTransaction) throws AccountTransactionException, AccountException;
}
