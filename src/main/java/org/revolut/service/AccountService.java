package org.revolut.service;

import com.google.inject.Inject;
import org.revolut.dao.AccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

public class AccountService {

    private final AccountDao accountDao;
    @Inject
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccount(long accountId) throws AccountException {
        return accountDao.findAccountById(accountId);
    }

    public long createAccount(AccountDto accountDto) throws AccountException {
        return accountDao.createAccount(accountDto).getAccountId();
    }

}
