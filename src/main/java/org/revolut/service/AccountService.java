package org.revolut.service;

import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

public interface AccountService {
     Account getAccount(long accountId) throws AccountException;
     long createAccount(AccountDto accountDto) throws AccountException;
}
