package org.revolut.dao;

import org.revolut.dto.AccountDto;
import org.revolut.model.Account;

public interface GenericDao {

    Account findAccountById(long accountId);

    Account createAccount(AccountDto accountDto);
}
