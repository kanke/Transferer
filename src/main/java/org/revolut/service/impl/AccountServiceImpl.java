package org.revolut.service.impl;

import com.google.inject.Inject;
import org.revolut.dao.AccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccount(long accountId) throws AccountException {
        return accountDao.findAccountById(accountId);
    }

    public long createAccount(AccountDto accountDto) throws AccountException {
        final Account account = accountDao.createAccount(accountDto);
        return account.getAccountId();
    }
}
