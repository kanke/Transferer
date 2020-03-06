package org.revolut.service;

import org.revolut.dto.AccountDto;
import org.revolut.model.Account;

public interface AccountService {
    Account getAccount(long accountId);
    long createAccount(AccountDto accountDto);
}
