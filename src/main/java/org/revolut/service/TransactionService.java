package org.revolut.service;

import org.revolut.dto.AccountTransactionDto;

public interface TransactionService {

    void transferFunds(AccountTransactionDto accountTransaction);
}
