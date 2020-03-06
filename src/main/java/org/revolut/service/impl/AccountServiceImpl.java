package org.revolut.service.impl;

import com.google.inject.Inject;
import org.revolut.dao.impl.InMemoryAccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.AccountService;

public class AccountServiceImpl implements AccountService{
    private final InMemoryAccountDao inMemoryAccountDao;

    @Inject
    public AccountServiceImpl(InMemoryAccountDao inMemoryAccountDao) {
        this.inMemoryAccountDao = inMemoryAccountDao;
    }

    public Account getAccount(long accountId) throws AccountException {
        return inMemoryAccountDao.findAccountById(accountId);
    }

    public long createAccount(AccountDto accountDto) throws AccountException {
        return inMemoryAccountDao.createAccount(accountDto).getAccountId();
    }
}
