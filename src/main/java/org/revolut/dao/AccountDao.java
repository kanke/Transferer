package org.revolut.dao;

import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

/**
 * This interface can be used to abstraction and decouple different implementations of Accounts as an entity
 */
public interface AccountDao {
    Account findAccountById(long accountId) throws AccountException;
    Account createAccount(AccountDto accountDto) throws AccountException;
}
