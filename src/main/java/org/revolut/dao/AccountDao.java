package org.revolut.dao;


import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

public interface AccountDao {
    Account findAccountById(long accountId) throws AccountException;
    Account createAccount(AccountDto accountDto) throws AccountException;
}
