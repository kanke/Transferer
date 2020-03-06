package org.revolut.service;

import com.google.inject.Inject;
import org.revolut.dao.InMemoryAccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

public class AccountService {
    private final InMemoryAccountDao inMemoryAccountDao;

    @Inject
    public AccountService(InMemoryAccountDao inMemoryAccountDao) {
        this.inMemoryAccountDao = inMemoryAccountDao;
    }

    public Account getAccount(long accountId) throws AccountException {
        return inMemoryAccountDao.findAccountById(accountId);
    }

    public long createAccount(AccountDto accountDto) throws AccountException {
        return inMemoryAccountDao.createAccount(accountDto).getAccountId();
    }
}
